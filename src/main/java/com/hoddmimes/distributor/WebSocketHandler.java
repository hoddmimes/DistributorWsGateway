package com.hoddmimes.distributor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.hoddmimes.distributor.generated.*;
import com.hoddmimes.jsontransform.MessageInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;



public class WebSocketHandler extends TextWebSocketHandler implements DistributorEventCallbackIf, DistributorUpdateCallbackIf {
    private  static  SimpleDateFormat SDF = new java.text.SimpleDateFormat("HH:mm:ss.SSS");
    private Logger                                  mLogger;
    private Distributor                             mDistributor;
    private Map<Integer, DistributorSubscriberIf>   mSubsciberConnections;
    private AtomicLong                              mSubscrRefId;
    private Map<Long, SubscriptionEntity>           mSubscrRefMap;
    private List<ClientSender>                      mClientSenders;
    private Map<String, WebSocketSession>           mAuthorizedSessions;
    private Map<String,WebSocketSession>            mWsSessionMap;
    private Configuration                           mConfiguration;



    public WebSocketHandler() {
        mConfiguration = Configuration.getInstance();
        mLogger = LogManager.getLogger( mConfiguration.getDistributorApplicationName());
        mSubsciberConnections = new HashMap<>();
        mSubscrRefId = new AtomicLong( System.currentTimeMillis());
        mSubscrRefMap = Collections.synchronizedMap( new HashMap<>());
        mAuthorizedSessions= Collections.synchronizedMap( new HashMap<>());
        mWsSessionMap = Collections.synchronizedMap(new HashMap<String, WebSocketSession>());

        initializeDistributor();
        setupClientSenderThreads();
    }

    /**
     * =================================================================================================================
     * Distributor Related Methods
     * =================================================================================================================
     */
    private void initializeDistributor() {
        DistributorApplicationConfiguration tDistributorApplicationConfiguration = new DistributorApplicationConfiguration(mConfiguration.getDistributorApplicationName());
        tDistributorApplicationConfiguration.setCMA(mConfiguration.getCmaAddress());
        tDistributorApplicationConfiguration.setCMAPort(mConfiguration.getCmaPort());
        tDistributorApplicationConfiguration.setLogFlags(mConfiguration.getLogFlags());
        tDistributorApplicationConfiguration.setEthDevice(mConfiguration.getEthDevice());
        tDistributorApplicationConfiguration.setLocalHostAddress(mConfiguration.getLocalIpAddress());

        try {
            mDistributor = new Distributor(tDistributorApplicationConfiguration);

            for (Configuration.McaEntry mca : mConfiguration.getMcaAddresses().values()) {
                DistributorConnectionConfiguration tConnConfig = new DistributorConnectionConfiguration(mca.getAddress(), mca.getPort());
                tConnConfig.setIpBufferSize(mConfiguration.getIpBufferSize());
                tConnConfig.setSegmentSize(mConfiguration.getSegmentSize());

                DistributorConnectionIf tDistributorConnection = mDistributor.createConnection(tConnConfig);
                mSubsciberConnections.put(mca.getGroupId(), mDistributor.createSubscriber(tDistributorConnection, this, this));
            }
        } catch (Exception e) {
            mLogger.fatal("Failed to declare Distributor", e);
            System.exit(0);
        }
    }

    private void setupClientSenderThreads() {
        mClientSenders = new ArrayList<>();

        for( int i = 0; i < mConfiguration.getClientSenderThreads(); i++) {
            ClientSender tSender = new ClientSender( (i+1));
            mClientSenders.add( tSender );
            tSender.start();
        }
    }


    @Override
    public void handleTextMessage(WebSocketSession pSession, TextMessage pMessage)
            throws InterruptedException, IOException {
        /**
         * Entry point for handling messages from web socket clients
         * Clients can send the following text messages (Json formatted)
         * - AddSubscriptionRqst, add subscription
         * - Remove SubscriptionRqst, removes one or more subscription
         */
        try {
            String tMsgString = pMessage.getPayload();
            JsonObject tJsonMessage = JsonParser.parseString(tMsgString).getAsJsonObject();
            String tMsgName = tJsonMessage.keySet().iterator().next(); // Get nessage name
            tJsonMessage = tJsonMessage.get( tMsgName ).getAsJsonObject();

            if (!tJsonMessage.has("rqstId")) {
                mLogger.warn("Request message is missing required attribute \"rqstId\" session: " + pSession.getRemoteAddress() );
                sendMessage( pSession, errorMsg("Request message is missing required attribute \"rqstId\"", null, -1));
            }

            int tRequestId = tJsonMessage.get("rqstId").getAsInt();

            if (mConfiguration.isUserAuthorizationEnabled() && (!tMsgName.equals("LoginRqst"))) {
                if (!isSessionAuthorized( pSession.getId())) {
                    mLogger.error("Session is unauthorized, request rejected, host: " + pSession.getRemoteAddress());
                    sendMessage(pSession, errorMsg("Unathorized session, request reject", null, tRequestId));
                    return;
                }
            }

            // Dispatch message
            if (tMsgName.equals("AddSubscriptionRqst")) {
                processAddSubscription(pSession, tMsgName, tRequestId, tJsonMessage);
            } else if (tMsgName.equals("LoginRqst")) {
                processLogin(pSession, tMsgName, tRequestId, tJsonMessage);
            } else if (tMsgName.equals("RemoveSubscriptionRqst")) {
                processRemoveSubscription(pSession, tMsgName, tRequestId, tJsonMessage);
            } else {
                mLogger.warn("Unknown request: " + pMessage);
                sendMessage( pSession, errorMsg("Unknown request: " + pMessage, null, tRequestId));
            }
        } catch (JsonSyntaxException je) {
            mLogger.error("Fail to parse request from: " + pSession.getRemoteAddress(), je);
            sendMessage(pSession, errorMsg("Failed to parse request", null, -1));
        }
    }

    private void  processLogin(WebSocketSession pWsSession, String pMsgName, int pRqstId, JsonObject pRqst ) {
        LoginRsp tRsp = new LoginRsp();
        tRsp.setRqstId(pRqstId);

        if (!mConfiguration.isUserAuthorizationEnabled()) {
            tRsp.setSuccess(true);
        } else if (mAuthorizedSessions.containsKey( pWsSession.getId())) {
            tRsp.setSuccess(true);
        } else {
           String tLoginUserName = pRqst.get("username").getAsString();
           String tLoginPassword = pRqst.get("password").getAsString();
           Configuration.AuthUser tUser = mConfiguration.getAuthorizedUsers().get( tLoginUserName.toUpperCase());
           if (tUser == null) {
               tRsp.setSuccess( false );
               mLogger.warn("Login FAILURE, user \"" + tLoginUserName + "\" is not defined, Host: " + pWsSession.getRemoteAddress());
           } else {
               if (!tUser.validate(tLoginUserName, tLoginPassword)) {
                   tRsp.setSuccess( false );
                   mLogger.warn("Login FAILURE, user \"" + tLoginUserName + "\" invalid password,  Host: " + pWsSession.getRemoteAddress());
               } else {
                   tRsp.setSuccess( true );
                   mAuthorizedSessions.put( pWsSession.getId(), pWsSession);
                   mLogger.info("Login SUCCESS, user \"" + tLoginUserName + "\",  Host: " + pWsSession.getRemoteAddress());
               }
           }
        }
        sendMessage( pWsSession, tRsp);
    }


    private void  processAddSubscription(WebSocketSession pWsSession, String pMsgName, int pRqstId, JsonObject pRqst ) {
        String tCallbackRef = (pRqst.has("callbackRef")) ? pRqst.get("callbackRef").getAsString() : null;
        String tSubjectId = pRqst.get("subjectId").getAsString();


        try {
            DistributorSubscriberIf tSubscriberIf = mSubsciberConnections.get( pRqst.get("groupId").getAsInt() );
            long tSubscrRefId = mSubscrRefId.incrementAndGet();

            Object tSubscrHandle = tSubscriberIf.addSubscription(tSubjectId, tSubscrRefId);

            mSubscrRefMap.put(tSubscrRefId, new SubscriptionEntity( tSubjectId, pWsSession, tCallbackRef, tSubscriberIf, tSubscrHandle, tSubscrRefId ));

            AddSubscriptionRsp tRsp = new AddSubscriptionRsp();
            tRsp.setRqstId( pRqstId );
            tRsp.setHandle( tSubscrRefId );
            sendMessage(pWsSession,tRsp);
        }
        catch( DistributorException e ) {
            mLogger.error("Fail to add subscription, subject: " + tSubjectId + " session: " + pWsSession.getRemoteAddress(), e);
            sendMessage(pWsSession, errorMsg("Failed to add subscription", e, pRqstId));
        }
    }


    private void  processRemoveSubscription(WebSocketSession pWsSession, String pMsgName, int pRqstId, JsonObject pRqst ) {

        try {
            SubscriptionEntity tSubscrEntity = mSubscrRefMap.remove(pRqst.get("handle").getAsLong());
            if (tSubscrEntity != null) {
                tSubscrEntity.mDistributorSubscriberIf.removeSubscription( tSubscrEntity.mDistributorHandle );
            }

            RemoveSubscriptionRsp tRsp = new RemoveSubscriptionRsp();
            tRsp.setRqstId( pRqstId );
            tRsp.setRemoved((tSubscrEntity != null));
            sendMessage(pWsSession,tRsp);
        }
        catch( DistributorException e ) {
            mLogger.error("Fail to remove subscription, session: " + pWsSession.getRemoteAddress(), e);
            sendMessage(pWsSession, errorMsg("Fail to remove subscription", e, pRqstId));
        }
    }

    private boolean isSessionAuthorized( String pSessionId ) {
        return (mAuthorizedSessions.containsKey(pSessionId));
    }

    private void sendMessage(WebSocketSession pSession, MessageInterface pMessageIf) {
        sendMessage( pSession, pMessageIf.toString());
    }



    private void sendMessage(WebSocketSession pSession, String pMessage) {
        TextMessage tMessage = new TextMessage( pMessage );
        try {
            pSession.sendMessage(tMessage);
        } catch (IOException e) {
            mLogger.warn("failed to send message to: " + pSession.getRemoteAddress());
        }
    }

    private void sendMessage(WebSocketSession pSession, JsonObject pJsonMessage) {
        sendMessage(pSession, pJsonMessage.toString() );
    }

    private void removeSubscriptions( String pWsSessionId ) {
        synchronized ( mSubscrRefMap ) {
            Iterator<Map.Entry<Long, SubscriptionEntity>> tItr = mSubscrRefMap.entrySet().iterator();
            while( tItr.hasNext() ) {
                Map.Entry<Long, SubscriptionEntity> tEntry = tItr.next();
                SubscriptionEntity se = tEntry.getValue();
                if (se.mWsSession.getId().equals( pWsSessionId )) {
                    try {se.mDistributorSubscriberIf.removeSubscription( se.mDistributorHandle );}
                    catch( DistributorException e) { e.printStackTrace();}
                    tItr.remove();
                }
            }
        }
    }

    private ErrorRsp errorMsg( String pMsg, Exception pException, int pRequestId  ) {
        ErrorRsp tError = new ErrorRsp();
        tError.setMessage( pMsg );
        tError.setRqstId( pRequestId );
        if (pException != null) {
            tError.setException( pException.getMessage());
        }
        return tError;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession pSession) throws Exception {
        StringBuilder esb  = new StringBuilder();

        //the messages will be broadcasted to all users.
        List<WebSocketExtension> tExtensions = pSession.getExtensions();
        for( WebSocketExtension tExtention : tExtensions) {
            esb.append("\n          " + tExtention.getName());
            for( Map.Entry<String,String> tParam : tExtention.getParameters().entrySet()) {
                esb.append("\n               " + tParam.getKey() + "  :  " + tParam.getValue());
            }
        }
        mLogger.info("WebClient Connected Host: " + pSession.getRemoteAddress() + " id: " + pSession.getId() + esb.toString());

        mWsSessionMap.put(pSession.getId(), pSession);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        mLogger.warn("WebClient disconnected Host: " + session.getRemoteAddress() + " id: " + session.getId());
        mWsSessionMap.remove( session.getId() );
        mAuthorizedSessions.remove( session.getId());
        try { session.close(); }
        catch( Exception e) {}
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception
    {
        mLogger.warn("WebClient session closed Host: " + session.getRemoteAddress() + " id: " + session.getId());
        mWsSessionMap.remove( session.getId() );
        mAuthorizedSessions.remove( session.getId());
        removeSubscriptions( session.getId() );
    }

    @Override
    public void distributorUpdate( String pSubjectName, byte[] pData, Object pCallbackParameter, int pQueueLength )
    {
        long tSubscrRefId = (Long) pCallbackParameter;
        SubscriptionEntity tSubscriptionEntity = mSubscrRefMap.get( tSubscrRefId );
        ClientSender tSender = (mClientSenders.size() == 1) ? mClientSenders.get(0) :
                                mClientSenders.get( Math.abs((tSubscriptionEntity.mWsSession.getId().hashCode() % mClientSenders.size())) );

        tSender.add( new SubscriptionUpdate( tSubscriptionEntity, pData, pQueueLength, tSender.getQueueLength()));
    }

    @Override
    public void distributorEventCallback(DistributorEvent pDistributorEvent) {
        mLogger.warn("DistributorEvent, signal: " + pDistributorEvent.getEventType().name() + " event: " + pDistributorEvent.toString());
    }


    public class ClientSender extends Thread
    {

        int mIndex;
        Base64.Encoder b64Encoder;
        LinkedBlockingDeque<SubscriptionUpdate> mUpdateEnities;

        public ClientSender( int pIndex ) {
            b64Encoder = Base64.getEncoder();
            mIndex = pIndex;
            mUpdateEnities = new LinkedBlockingDeque<>();
        }

        public void add( SubscriptionUpdate pUpdate ) {
            synchronized ( mUpdateEnities ) {
                try {mUpdateEnities.put( pUpdate ); }
                catch( InterruptedException e){
                    e.printStackTrace();
                }
            }
        }

        public int getQueueLength() {
            return mUpdateEnities.size();
        }

        private void send( SubscriptionUpdate tUpdate ) {
            if (tUpdate == null) {
                return;
            }

            if (mWsSessionMap.containsKey( tUpdate.mSubscriptionEntity.mWsSession.getId() )) {
                Update tBdx = new Update();
                tBdx.setDistQueLen( tUpdate.mDistrQueLen );
                tBdx.setSndrQueLen( tUpdate.mSenderQueLen );
                tBdx.setCallbackRef( tUpdate.mSubscriptionEntity.mCallbackRef );
                tBdx.setHandle( tUpdate.mSubscriptionEntity.mSubscrRefId );
                tBdx.setSubjectId(tUpdate.mSubscriptionEntity.mSubjectId);
                tBdx.setTime( tUpdate.mTime );
                if (mConfiguration.isPayloadB64()) {
                    tBdx.setPayload( b64Encoder.encodeToString( tUpdate.mPayLoad ));
                } else {
                    tBdx.setPayload( new String( tUpdate.mPayLoad ));
                }
                sendMessage( tUpdate.mSubscriptionEntity.mWsSession, tBdx);
            }
        }

        public void run() {
            List<SubscriptionUpdate> tUpdLst = new ArrayList<>(20);
            setName("ClientSender-" + mIndex );
            while( true ) {
                synchronized (mUpdateEnities) {
                    tUpdLst.clear();
                    mUpdateEnities.drainTo( tUpdLst, 20);
                }

                if (tUpdLst.size() == 0) {
                    SubscriptionUpdate tUpd = null;
                    try {tUpd = mUpdateEnities.take();}
                    catch( InterruptedException ie) {}
                    send( tUpd );
                } else {
                    for( SubscriptionUpdate tUpd : tUpdLst ) {
                        send( tUpd );
                    }
                }
            }
        }
    }



    public class SubscriptionEntity
    {
        String                      mCallbackRef; // User callback reference
        WebSocketSession            mWsSession;
        String                      mSubjectId;
        long                        mSubscrRefId;
        DistributorSubscriberIf     mDistributorSubscriberIf;
        Object                      mDistributorHandle;

        public  SubscriptionEntity( String pSubjectId, WebSocketSession pWsSession, String pCallbackRef,
                                    DistributorSubscriberIf pSubscriberIf, Object pDistributorHandle, long pSubscrRefId ) {
            mCallbackRef = pCallbackRef;
            mWsSession = pWsSession;
            mSubjectId = pSubjectId;
            mDistributorSubscriberIf = pSubscriberIf;
            mDistributorHandle = pDistributorHandle;
            mSubscrRefId = pSubscrRefId;
        }

    }

    class SubscriptionUpdate {
        SubscriptionEntity mSubscriptionEntity;
        byte[] mPayLoad;
        String mTime;
        int mDistrQueLen;
        int mSenderQueLen;

        SubscriptionUpdate( SubscriptionEntity pSubscriptionEntity, byte[] pPayLoad, int pDistrQueLen, int pSenderQueLen  ) {
            mSubscriptionEntity = pSubscriptionEntity;
            mPayLoad =  pPayLoad;
            mTime = SDF.format( System.currentTimeMillis());
            mDistrQueLen = pDistrQueLen;
            mSenderQueLen = pSenderQueLen;
        }
    }
}

