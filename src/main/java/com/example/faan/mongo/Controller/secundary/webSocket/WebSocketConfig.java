package com.example.faan.mongo.Controller.secundary.webSocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/faan-websocket");
        registry.addEndpoint("/faan-websocket").withSockJS();
        registry.addEndpoint("/faan-websocket").setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // <- Permite enviar mensajes a los clientes suscritos a /topic
        registry.setApplicationDestinationPrefixes("/app"); // <- Prefijo para los mensajes que se envían desde los clientes al servidor
    }

}