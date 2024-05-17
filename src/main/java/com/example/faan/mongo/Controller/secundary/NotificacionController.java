package com.example.faan.mongo.Controller.secundary;

import com.example.faan.mongo.Service.Mi_UbicacionService;
import com.example.faan.mongo.Service.mongoService.NotificacionAutomaticaService;
import com.example.faan.mongo.modelos.secundary.Mi_Ubicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionAutomaticaService notificacionService;
    private final Mi_UbicacionService ubicacionService;

    @Autowired
    public NotificacionController(NotificacionAutomaticaService notificacionService, Mi_UbicacionService ubicacionService) {
        this.notificacionService = notificacionService;
        this.ubicacionService = ubicacionService;
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarNotificaciones() {
        try {
            notificacionService.verificarNotificaciones();
            return ResponseEntity.status(HttpStatus.OK).body("Notificaciones enviadas correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar las notificaciones.");
        }
    }

    @GetMapping("/todas")
    public ResponseEntity<List<Mi_Ubicacion>> obtenerTodasLasUbicaciones() {
        List<Mi_Ubicacion> ubicaciones = ubicacionService.obtenerTodasLasUbicaciones();
        return ResponseEntity.ok(ubicaciones);
    }
}
