package com.gmart.api.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.gmart.api.chat.interceptors.HttpHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer{
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api")
                .setAllowedOrigins("*")
                .withSockJS().setInterceptors(new HttpHandshakeInterceptor());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
    	 registry.setApplicationDestinationPrefixes("/app");
         registry.enableSimpleBroker("KAFKA_TP","/topic","/queue", "/chats");
         
//         registry.setApplicationDestinationPrefixes("/app");
//
//         // Use this for enabling a Full featured broker like RabbitMQ
//         registry.enableStompBrokerRelay("/topic","/queue","/chats")
//                 .setRelayHost("localhost")
//                 .setRelayPort(61613)
//                 .setClientLogin("guest")
//                 .setClientPasscode("guest");
     
    }
}