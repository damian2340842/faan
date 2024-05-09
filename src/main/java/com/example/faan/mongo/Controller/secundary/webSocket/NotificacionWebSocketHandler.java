package com.example.faan.mongo.Controller.secundary.webSocket;

import com.example.faan.mongo.Repository.secundary.NotificacionRepository;
import com.example.faan.mongo.modelos.secundary.Notificacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Deprecated
public class NotificacionWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        // Lógica adicional después de que se establece la conexión
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        try {
            JSONObject jsonObject = new JSONObject(payload);
            // Lógica para manejar el mensaje JSON recibido
        } catch (Exception e) {
            System.out.println("Error al procesar el mensaje JSON: " + e.getMessage());
        }
    }

    public void enviarNotificacionesAUsuarios(List<Notificacion> notificaciones) {
        Iterator<WebSocketSession> iterator = sessions.iterator();
        while (iterator.hasNext()) {
            WebSocketSession session = iterator.next();
            try {
                if (session.isOpen()) {
                    TextMessage message = new TextMessage(objectMapper.writeValueAsString(notificaciones));
                    session.sendMessage(message);
                } else {
                    iterator.remove(); // Remover sesiones cerradas
                }
            } catch (IOException e) {
                System.out.println("Error al enviar notificación: " + e.getMessage());
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void enviarNotificacionesPeriodicamente() {
        // Lógica para obtener y enviar notificaciones periódicamente
    }
}
