package com.example.faan.mongo.Service.mongoService;

import com.example.faan.mongo.Service.Mi_UbicacionService;
import com.example.faan.mongo.Service.PublicacionService;
import com.example.faan.mongo.Service.UsuarioService;
import com.example.faan.mongo.modelos.Publicacion;
import com.example.faan.mongo.modelos.Usuario;
import com.example.faan.mongo.modelos.secundary.Location;
import com.example.faan.mongo.modelos.secundary.Mi_Ubicacion;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificacionAutomaticaService {

    private final UsuarioService usuarioService;
    private final PublicacionService publicacionService;
    private final Mi_UbicacionService mi_UbicacionService;

    public NotificacionAutomaticaService(UsuarioService usuarioService, PublicacionService publicacionService, Mi_UbicacionService mi_UbicacionService) {
        this.usuarioService = usuarioService;
        this.publicacionService = publicacionService;
        this.mi_UbicacionService = mi_UbicacionService;
    }

    @Scheduled(fixedRate = 60000) // Ejecutar cada minuto
    public void verificarNotificaciones() {
        List<Mi_Ubicacion> ubicacionesUsuarios = mi_UbicacionService.obtenerTodasLasUbicaciones();
        List<Publicacion> publicaciones = publicacionService.obtenerTodasLasPublicaciones();

        for (Mi_Ubicacion ubicacionUsuario : ubicacionesUsuarios) {
            Location locationUsuario = new Location(ubicacionUsuario.getLatitude(), ubicacionUsuario.getLongitude());
            for (Publicacion publicacion : publicaciones) {
                if (calcularDistancia(locationUsuario, publicacion.getLocation()) <= 5) { // 5 km
                    enviarNotificacion(ubicacionUsuario.getUserId(), publicacion);
                }
            }
        }
    }

    private void enviarNotificacion(Usuario usuario, Publicacion publicacion) {
        System.out.println("Enviando notificación a usuario: " + usuario.getUsername() + " sobre la publicación: " + publicacion.getNombre());
    }
    ///listar
    // Resto del código omitido para mayor claridad...

    public List<Usuario> obtenerUsuariosCercanosAPublicacion(Publicacion publicacion) {
        List<Usuario> usuariosCercanos = new ArrayList<>();
        List<Mi_Ubicacion> ubicacionesUsuarios = mi_UbicacionService.obtenerTodasLasUbicaciones();

        for (Mi_Ubicacion ubicacionUsuario : ubicacionesUsuarios) {
            Location locationUsuario = new Location(ubicacionUsuario.getLatitude(), ubicacionUsuario.getLongitude());

            if (calcularDistancia(locationUsuario, publicacion.getLocation()) <= 5) {
                usuariosCercanos.add(ubicacionUsuario.getUserId());
            }
        }

        return usuariosCercanos;
    }

    private double calcularDistancia(Location ubicacion1, Location ubicacion2) {
        double radioTierra = 6371; // Radio de la Tierra en kilómetros
        double latitud1 = Math.toRadians(ubicacion1.getLatitude());
        double latitud2 = Math.toRadians(ubicacion2.getLatitude());
        double longitud1 = Math.toRadians(ubicacion1.getLongitude());
        double longitud2 = Math.toRadians(ubicacion2.getLongitude());

        double deltaLatitud = latitud2 - latitud1;
        double deltaLongitud = longitud2 - longitud1;

        double a = Math.sin(deltaLatitud / 2) * Math.sin(deltaLatitud / 2)
                + Math.cos(latitud1) * Math.cos(latitud2)
                * Math.sin(deltaLongitud / 2) * Math.sin(deltaLongitud / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        System.out.println("ubi " +radioTierra * c);
        return radioTierra * c;
    }
}
