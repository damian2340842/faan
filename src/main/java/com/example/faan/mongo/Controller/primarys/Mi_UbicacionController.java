package com.example.faan.mongo.Controller.primarys;

import com.example.faan.mongo.Service.Mi_UbicacionService;
import com.example.faan.mongo.modelos.secundary.Mi_Ubicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ubicaciones")
public class Mi_UbicacionController {

    private final Mi_UbicacionService mi_UbicacionService;

    @Autowired
    public Mi_UbicacionController(Mi_UbicacionService mi_UbicacionService) {
        this.mi_UbicacionService = mi_UbicacionService;
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Mi_Ubicacion> guardarUbicacion(@RequestBody Mi_Ubicacion ubicacion) {
        try {
            Mi_Ubicacion guardadaUbicacion = mi_UbicacionService.guardarUbicacion(ubicacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardadaUbicacion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}