# DistributorWsGateway

The _DistributorWsGateway_ a.k.a DWG is a application server allowing WEB clients to subscribe to [_Distributor_](https://github.com/hoddmimes/Distributor) 
real time broadcast using  [WebSocket](https://en.wikipedia.org/wiki/WebSocket).

![alt text](https://github.com/hoddmimes/DistributorWsGateway/blob/master/doc/DWG.png "Schematic solution")

##Overview
The _Distributor_ is a framework allowing applications to publish / subscribe real time message flows. 
The the Distributor framework use IP multicast to exchange information. 

The _DWG_ component is an application gateway allowing WEB applications to subscribe to real time information published
by Distributor publisher applications. 

The _DWG_ gateway acts as a Distributor subscriber application and proxy server for WEB applications that like obtain 
real time information published by publisher applications.

WEB applications connects to the _DWG_ using the WebSockets protokoll.  In order for WEB applications to receive 
updates the must submit _subscriptions_ defining what information they have an interest in.

The _DWG_ will maintain the subscriptions the WEB apps have submited. When the _DWG_ app receives updates, 
it forwared the update to the WEB application having the interest in the information.

## DWG Configuration 
The _DWG_ server takes the following args 
"-configuration _<dwg-configuration-file>_"

An example of a DWG is found below

<small>

```

   <DistributorWebSocketGateway
       appName = "WsGateway"
       clientSenderThreads = "2"
       localIpAddress = "192.168.42.100"
       cmaAddress = "224.42.42.100"
       cmaPort = "4242"
       ipBufferSize = "128000"
       segmentSize = "8192"
       payloadB64 = "true"
       sslKeyStore = "distributor-keystore.jks"
       keyStoreAlias = "distributor"
       keyStorePassword = "sesame"
       httpPort = "8888">
       
       <MulticastGroups>
           <Group address="224.42.42.1" port="4201" groupId="1"/>
           <Group address="224.66.66.1" port="4201" groupId="2"/>
       </MulticastGroups>
   
       <AllowedClients enabled="true">
           <Host ipAddress="192.168.1.0/24"/>
           <Host ipAddress="192.168.42.0/24"/>
       </AllowedClients>
   
       <AuthorizedUsers enabled="true">
           <User username="joshua" password="tictactoe"/>
           <User username="test" password="test"/>
       </AuthorizedUsers>
    </DistributorWebSocketGateway>   

```
</small>

####Attributes
<small>

* __appName__, name of of the application when announce itself as a _Distributor_ application.
* __clientSenderThreads__, number of threads being used to send update messages to WEB apps. A WEB app will always be served by the same sender thread in order to guarantee message order.
* __localIpAddress__, indetify the network interface on which the _DWG_ app will use when communicate with other _Distributor_ applications.
* __cmaAddress__, IP mulicast address the _DWG_ uses when exchanging controll messages with other _Distributor_ applications.
* __cmaPort__, IP UDP port the _DWG_ uses when exchanging control messages with other _Distributor_ applications.
* __ipBufferSize__, the IP buffer size used for the _Distributor_ IP sockets used. _(no reason to change)_
* __segmentSize__, the segment used when exchange / distributing  _Distributor_ messages. _(no reason to change)_
* __payloadB64__, if applications messages distributed with the _Distributor_ frawework are binary it will be a good idea to set this flag to true. Then the _DWG_ will B64 encode the updates.
* __sslKeyStore__, key store to be used if the WebSockets are to be SSL encrypted. *__This attribute is optional__*. If not being present WS sessions will be uncrypted.
* __keyStoreAlias__, key store alias name.
* __keyStorePassword__, key store password
* __httpPort__, HTTP port on which the _DWG_ gateway will accept WS connections.
* __MulticastGroups__, IP multicast groups used when interacting with other _Distributor_ applications.
    * __address__ IP multicast address (i.e. IP class D address). 
    * __port__ IP multicast / UDP port.
    * __groupId__ identifies the multicast group, used by WEB apps to address where a subscription should be applied.
* __AllowedClients__ If possible to restrict what host that should be allowed to connect to the _DWG_. The validation check can be enabled/disabled by setting the _enabled_ attribute.
    ** __ipAddress__ the ipaddress or a range of host addresses that should be allowed to connect. The user authorization check can be enabled/diabled by setting the _enabled_ attribute.
* __AuthorizedUsers__ it is possible to enable authorization on a _user_ basis.  
    * __username__ authorized fictive user identity.
    * __password__ password for the user. 
</small>
  
 ##WEB App

WEB applications should use an appropiate WebSocket API.
The WEB can and should send a few messages inorder to receive updates.
The messages are defined in the file [DistributorMessages.xml](https://github.com/hoddmimes/DistributorWsGateway/blob/master/xml/DistributorMessage.xml)
All messages being sent and received are Json encoded. 
An example of how to interact with a _DWG_ are found in the file [distributor.html](https://github.com/hoddmimes/DistributorWsGateway/blob/master/public/distributor.html)

__Messages__
 
 <small>
 
 * __AddSubscriptionRqst__, message used to register an interest in a message flow. Without setting up a subscription no information will be received.
    * __rqstId__, identifies the request. Used to pair the response 
    * __subjectId__, identifies the information being of interest for the application. Subjects are string with an hierarchical  name structure e.g /foo/bar/fie More info about subject identities see [Subject Names](https://github.com/hoddmimes/Distributor/blob/master/README.md#subject-names)
    * __groupId__, identifies the IP multicast group where the information is published.
    * __callbackRef__, user defined parameter passed back in the unsolicited update sent out when the subject is updated.
 * __RemoveSubscriptionRqst__, removes a previously added subscription
    * __rqstId__, identifies the request. Used to pair the response 
    * __handle__, idetifies the subscriptio. The handle is returned in the *AddSubscriptionRsp*
 * __LoginRqst__, must be sent as first message in case user authorization is enabled in the _DWG_
    * __username__, user identity to use when authorizing the session
    * __password__, password for the user.
    
 * __Update__, send unsolicited from the _DWG_ to the WEB app when information that the app has an interest is updated.
    * __subjectId__ subject idetifying the data being updated.
    * __handle__ the subscription _handle_ associated with the subscription causing the update to be sent. The handle is returned in the *AddSubscriptionRsp*
    * __time__ the time in the _DWG_ when the update was queued. Format "HH::mm:ss.SSS"
    * __distQueLen__ inbound queue length to the _DWG_ on the receiver side.
    * __sndrQueLen__ outbound queue length on the sender side to the WEB app when the update was queued.
    * __payload__ data being update. (can be B64 encoded depending on the _DWG_ configuration).
    * __callbackRef__ user callback reference defined when the subscription was setup.  
     
 </small>