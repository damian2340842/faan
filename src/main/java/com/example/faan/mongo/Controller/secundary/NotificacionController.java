package com.example.faan.mongo.Controller.secundary;

import com.example.faan.mongo.Service.mongoService.NotificacionService;
import com.example.faan.mongo.modelos.secundary.Notificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @PostMapping("/notificaciones/save")
    public ResponseEntity<Notificacion> save(@RequestBody Notificacion notificacion) {
        return new ResponseEntity<>(notificacionService.save(notificacion), HttpStatus.CREATED);
    }

    @GetMapping("/findById/{id}") // Corrección en la ruta completa
    public ResponseEntity<Notificacion> findById(@PathVariable("id") String id) {
        return new ResponseEntity<>(notificacionService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/updateEstado/notificaciones/{id}") // Corrección en la ruta completa
    public ResponseEntity<Notificacion> updateEstado(@PathVariable("id") String id) {
        Notificacion notificacion = notificacionService.findById(id);
        return new ResponseEntity<>(notificacionService.update(id, null), HttpStatus.OK);
    }
}
