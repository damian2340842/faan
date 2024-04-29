package com.example.faan.mongo.Controller;

import com.example.faan.mongo.modelos.Publicacion;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketPublicacionController {

    @MessageMapping("/nuevaPublicacion")
    @SendTo("/topic/publicaciones")
    public Publicacion nuevaPublicacion(Publicacion publicacion) {
        // Procesar la nueva publicación
        return publicacion;
    }

    @MessageExceptionHandler
    public void handleException(Throwable exception) {
        // Maneja la excepción aquí, puedes registrarla, enviar una respuesta específica, etc.
        System.out.println("Se produjo una excepción en el controlador de publicaciones WebSocket: " + exception.getMessage());
    }
}
