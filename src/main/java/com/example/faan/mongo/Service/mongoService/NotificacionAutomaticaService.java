package com.example.faan.mongo.Service.mongoService;

import com.example.faan.mongo.Service.PublicacionService;
import com.example.faan.mongo.Service.UsuarioService;
import com.example.faan.mongo.modelos.Publicacion;
import com.example.faan.mongo.modelos.Usuario;
import com.example.faan.mongo.modelos.secundary.Mi_Ubicacion;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionAutomaticaService {

    private final UsuarioService usuarioService;
    private final PublicacionService publicacionService;

    public NotificacionAutomaticaService(UsuarioService usuarioService, PublicacionService publicacionService) {
        this.usuarioService = usuarioService;
        this.publicacionService = publicacionService;
    }

    @Scheduled(fixedRate = 60000) // Ejecutar cada minuto
    public void verificarNotificaciones() {
        List<Usuario> usuarios = usuarioService.findAll();
        List<Publicacion> publicaciones = publicacionService.obtenerTodasLasPublicaciones();

        for (Usuario usuario : usuarios) {
            Mi_Ubicacion ubicacionUsuario = new Mi_Ubicacion().; // Asume que el usuario tiene una ubicación asociada
            for (Publicacion publicacion : publicaciones) {
                if (calcularDistancia(ubicacionUsuario, publicacion.getUbicacion()) <= 5) { // 5 km
                    enviarNotificacion(usuario, publicacion);
                }
            }
        }
    }

    private void enviarNotificacion(Usuario usuario, Publicacion publicacion) {
        // Implementa la lógica para enviar la notificación al usuario (por ejemplo, correo electrónico, SMS, notificación push, etc.)
        System.out.println("Enviando notificación a usuario: " + usuario.getUsername() + " sobre la publicación: " + publicacion.getTitulo());
    }

    private double calcularDistancia(Ubicacion ubicacion1, Ubicacion ubicacion2) {
        double radioTierra = 6371; // Radio de la Tierra en kilómetros
        double latitud1 = Math.toRadians(ubicacion1.getLatitud());
        double latitud2 = Math.toRadians(ubicacion2.getLatitud());
        double longitud1 = Math.toRadians(ubicacion1.getLongitud());
        double longitud2 = Math.toRadians(ubicacion2.getLongitud());

        double deltaLatitud = latitud2 - latitud1;
        double deltaLongitud = longitud2 - longitud1;

        double a = Math.sin(deltaLatitud / 2) * Math.sin(deltaLatitud / 2)
                + Math.cos(latitud1) * Math.cos(latitud2)
                * Math.sin(deltaLongitud / 2) * Math.sin(deltaLongitud / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return radioTierra * c;
    }
}

}

