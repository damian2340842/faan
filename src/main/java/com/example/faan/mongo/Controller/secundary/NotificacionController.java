package com.example.faan.mongo.Controller.secundary;

import com.example.faan.mongo.Service.mongoService.NotificacionService;
import com.example.faan.mongo.modelos.secundary.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @PostMapping("/guardar")
    public ResponseEntity<?> save(@RequestBody Notificacion notificacion) {
        try {
            Notificacion savedNotificacion = notificacionService.save(notificacion);
            return ResponseEntity.status(HttpStatus.CREATED).body("La notificación se ha guardado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo guardar la notificación. Por favor, inténtelo de nuevo más tarde.");
        }
    }


    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById2(@PathVariable("id") String id) {
        try {
            Notificacion notificacion = notificacionService.findById(id);
            if (notificacion != null) {

                return ResponseEntity.status(HttpStatus.OK).body(notificacion);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna notificación con el ID: " + id);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo guardar la notificación. Por favor, inténtelo de nuevo más tarde.");
    }


    @PutMapping("/updateEstado/{id}")
    public ResponseEntity<?> updateEstado(@PathVariable("id") String id, @RequestBody Notificacion notificacion) {
        try {
            // Verificar si la notificación con el ID proporcionado existe
            Notificacion notificacionExistente = notificacionService.findById(id);
            if (notificacionExistente != null) {
                // Actualizar el estado de la notificación
                Notificacion notificacionActualizada = notificacionService.update(id, notificacion);
                return ResponseEntity.status(HttpStatus.OK).body("Notificacion actualizada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna notificación con el ID: " + id);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo guardar la notificación. Por favor, inténtelo de nuevo más tarde.");
    }


}
