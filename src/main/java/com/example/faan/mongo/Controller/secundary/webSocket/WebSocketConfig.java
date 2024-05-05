package com.example.faan.mongo.Controller.secundary.webSocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

        @Override
        public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
            registry.addHandler(myWebSocketHandler(), "/my-websocket-endpoint")
                    .setAllowedOriginPatterns("http://localhost:4200");
        }

        @Bean
        public WebSocketHandler myWebSocketHandler( ) {
            return new NotificacionWebSocketHandler();
        }

    }

