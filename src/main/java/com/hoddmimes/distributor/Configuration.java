package com.hoddmimes.distributor;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * This class is used for customizing Gateway parameter
 * environment as well as application parameters
 */


public class Configuration {
    private static volatile Configuration cInstance = null;


    // Environment Attributes
    private int mHttpPort = 8080;
    private String mSSLKeyStore = null;
    private String mKeyStoreAlias = null;
    private String mKeyStorePassword = null;
    private int    mClientSenderThreads = 2;
    private boolean mPayloadB64 = false;
    private List<String> mAllowedClientAddresses = null;
    private Map<String,AuthUser> mAuthorizedUsers = null;


    // Distributor Attributes
    private String mAppName = "DistributorWebSocketGateway";
    private String mCmaAddress = DistributorApplicationConfiguration.DEFAULT_CMA_ADDRESS;
    private int mCmaPort = DistributorApplicationConfiguration.DEAFULT_CMA_PORT;
    private Map<Integer, McaEntry> mMcaAddresses = new HashMap<>();
    private int mIpBufferSize = 128000;
    private int mSegmentSize = 8192;
    private String mEthDevice = "eth0";
    private String mLocalIpAddress = null;
    private int mLogFlags = DistributorApplicationConfiguration.LOG_CONNECTION_EVENTS +
            DistributorApplicationConfiguration.LOG_RETRANSMISSION_EVENTS +
            DistributorApplicationConfiguration.LOG_SUBSCRIPTION_EVENTS +
            DistributorApplicationConfiguration.LOG_RMTDB_EVENTS;


    private Configuration() {
    }

    public static synchronized Configuration getInstance() {
        if (cInstance == null) {
            cInstance = new Configuration();
        }
        return cInstance;
    }

    public void setClientSenderThreads( int pThreads ) {
        mClientSenderThreads = pThreads;
    }

    public int getClientSenderThreads() {
        return mClientSenderThreads;
    }

    public void setDistributorAppName(String pName) {
        mAppName = pName;
    }

    public String getLocalIpAddress() {
        return mLocalIpAddress;
    }

    public void setLocalIpAddress(String pAddress) {
        mLocalIpAddress = pAddress;
    }

    public void addMca(String pAddress, int pPort, int pGroupId) {
        mMcaAddresses.put(pGroupId, new McaEntry(pAddress, pPort, pGroupId));
    }

    public int getHttpPort() {
        return mHttpPort;
    }

    public void setHttpPort(int pPort) {
        mHttpPort = pPort;
    }

    public Map<Integer, McaEntry> getMcaAddresses() {
        return mMcaAddresses;
    }

    public String getEthDevice() {
        return mEthDevice;
    }

    public void setEthDevice(String pEthDevice) {
        mEthDevice = pEthDevice;
    }

    public int getIpBufferSize() {
        return mIpBufferSize;
    }

    public void setIpBufferSize(int pBufferSize) {
        mIpBufferSize = pBufferSize;
    }

    public int getSegmentSize() {
        return mSegmentSize;
    }

    public void setSegmentSize(int pSegmentSize) {
        mSegmentSize = pSegmentSize;
    }

    public boolean isSSLEnabled() {
        return (mSSLKeyStore != null);
    }

    public String getSSLKeyStore() {
        return mSSLKeyStore;
    }

    public void setSSLKeyStore(String pKeyStore) {
        mSSLKeyStore = pKeyStore;
    }

    public String getKeyStoreAlias() {
        return mKeyStoreAlias;
    }

    public void setKeyStoreAlias(String pAlias) {
        mKeyStoreAlias = pAlias;
    }

    public String getKeyStorePassword() {
        return mKeyStorePassword;
    }

    public void setKeyStorePassword(String pPassword) {
        mKeyStorePassword = pPassword;
    }

    public int getLogFlags() {
        return mLogFlags;
    }

    public void setLogFlags(int pLogMask) {
        mLogFlags = pLogMask;
    }

    public int getCmaPort() {
        return mCmaPort;
    }

    public void setCmaPort(int pPort) {
        mCmaPort = pPort;
    }

    public String getCmaAddress() {
        return mCmaAddress;
    }

    public void setCmaAddress(String pAddress) {
        mCmaAddress = pAddress;
    }

    public String getDistributorApplicationName() {
        return mAppName;
    }

    public boolean isPayloadB64() {
        return mPayloadB64;
    }

    public void addAuthorizedUser( String pUser, String pPassword ) {
       if (mAuthorizedUsers == null) {
           mAuthorizedUsers = new HashMap<>();
       }
       mAuthorizedUsers.put( pUser.toUpperCase(), new AuthUser( pUser, pPassword ));
    }

    public Map<String, AuthUser> getAuthorizedUsers() {
        return mAuthorizedUsers;
    }

    public boolean isUserAuthorizationEnabled() {
        return (mAuthorizedUsers != null);
    }

    public void addAllowedClientAddresses( String pAddr ) {
        if (mAllowedClientAddresses == null) {
            mAllowedClientAddresses = new ArrayList<>();
        }
        mAllowedClientAddresses.add( pAddr );
    }

    public List<String> getAllowedClientAddresses() {
        return mAllowedClientAddresses;
    }

    public void setPayloadB64(boolean pPayloadB64) {
        mPayloadB64 = pPayloadB64;
    }

    public static class AuthUser
    {
        private String mName;
        private String mPassword;

        public AuthUser( String pName, String pPassword ) {
            mName = pName;
            mPassword = pPassword;
        }

        public boolean validate( String pLoginUser, String pLoginPassword ) {
            if ((pLoginUser == null) || (pLoginPassword == null)) {
                return false;
            }
            if (!pLoginPassword.equalsIgnoreCase( mName)) {
                return false;
            }
            if (!pLoginPassword.equals( mPassword )) {
                return false;
            }
            return true;
        }
    }
    public static class McaEntry {
        private String mMcaAddress;
        private int mMcaPort;
        private int mGroupId;

        public McaEntry(String pMca, int mPort, int pGroupId) {
            mMcaAddress = pMca;
            mMcaPort = mPort;
            mGroupId = pGroupId;
        }

        public int getGroupId() {
            return mGroupId;
        }

        public int getPort() {
            return mMcaPort;
        }

        public String getAddress() {
            return mMcaAddress;
        }
    }
}
