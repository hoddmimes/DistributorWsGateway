package com.hoddmimes.distributor;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        WebSocketHandlerRegistration tWsHandler = registry.addHandler(new WebSocketHandler(), "/distributor");
        tWsHandler.addInterceptors( new DistributorHandshakeInterceptor());
    }
}
