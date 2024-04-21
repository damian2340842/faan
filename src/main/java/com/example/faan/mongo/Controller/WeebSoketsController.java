package com.example.faan.mongo.Controller;

import com.example.faan.mongo.modelos.Publicacion;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Controller
public class WeebSoketsController extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        // Evento activado cuando se establece la conexión WebSocket
        System.out.println("Conexión establecida correctamente.");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        // Evento activado cuando se recibe un mensaje desde el cliente
        System.out.println("Mensaje recibido: " + message.getPayload());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        // Evento activado cuando hay un error en la conexión WebSocket
        System.out.println("Error en la conexión WebSocket: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        // Evento activado cuando se cierra la conexión WebSocket
        System.out.println("Conexión cerrada.");
    }

}



