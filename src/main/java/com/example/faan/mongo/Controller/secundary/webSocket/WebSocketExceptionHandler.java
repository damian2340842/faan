package com.example.faan.mongo.Controller.secundary.webSocket;


import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Deprecated
public class WebSocketExceptionHandler {
        @MessageExceptionHandler
        public void handleException(Throwable exception) {
            // Maneja la excepción aquí, puedes registrarla, enviar una respuesta específica, etc.
            System.out.println("Se produjo una excepción en el WebSocket: " + exception.getMessage());
        }
    }


