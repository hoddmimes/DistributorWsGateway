package com.hoddmimes.distributor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.List;


public class DistributorHandshakeInterceptor extends HttpSessionHandshakeInterceptor
{
    @Override
    public boolean beforeHandshake( ServerHttpRequest   pRequest,
                                    ServerHttpResponse  pResponse,
                                    WebSocketHandler    pWsHandler,
                                    Map<String, Object> pAttributes) throws Exception
    {
        boolean tRetStatus = super.beforeHandshake( pRequest, pResponse, pWsHandler, pAttributes );


        if (tRetStatus) {
           InetAddress  tInetAddr = InetAddress.getByName(pRequest.getRemoteAddress().getHostName());
           Configuration tConfig = Configuration.getInstance();
           List<String> tAllowedClientAddresses = tConfig.getAllowedClientAddresses();
           if (tAllowedClientAddresses != null) {
               tRetStatus = false;
               for(String tAllowedAddr : tAllowedClientAddresses) {
                   if (matchAdress( tInetAddr.getHostAddress(), tAllowedAddr)) {
                       tRetStatus = true;
                   }
               }
               Logger mLogger = LogManager.getLogger( tConfig.getDistributorApplicationName());
               if (!tRetStatus) {
                   mLogger.warn("Connection Authorized FAILURE, host (ip address) " + tInetAddr.getHostAddress() + " is NOT authorized ");
               } else {
                   mLogger.warn("Connection Authorized SUCCESS, host (ip address) " + tInetAddr.getHostAddress() + " is authorized ");
               }
           }
        }
        return tRetStatus;
    }


    private boolean matchAdress(String ip, String subnet) {
        IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(subnet);
        return ipAddressMatcher.matches(ip);
    }
}
