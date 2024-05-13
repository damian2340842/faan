package com.example.faan.mongo.publicidad.controller;

import com.example.faan.mongo.modelos.EnumsFijo.TipoPublicacion;
import com.example.faan.mongo.modelos.Publicacion;
import com.example.faan.mongo.publicidad.model.Publicidad;
import com.example.faan.mongo.publicidad.service.PublicidadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/publicidades")
public class PublicidadController {

    private final PublicidadService publicidadService;

    public PublicidadController(PublicidadService publicidadService) {
        this.publicidadService = publicidadService;
    }


    @GetMapping("/listar")
    public ResponseEntity<?> listarPublicacionesFavoritos() {
        List<Publicidad> publicidades = publicidadService.obtenerTodasLasPublicidades();

        if (publicidades.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron publicaciones favoritas.");
        }

        return ResponseEntity.ok(publicidades);
    }



    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPublicidadPorId(@PathVariable BigInteger id) {
        Optional<Publicidad> publicidadOpt = publicidadService.obtenerPublicidadPorId(id);
        if (publicidadOpt.isPresent()) {
            return ResponseEntity.ok(publicidadOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la publicidad con el ID seleccionado.");
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearPublicidad(@RequestPart("publicidad") Publicidad publicidad, @RequestPart(value = "photo", required = false) MultipartFile photo) throws IOException {

        Publicidad nuevaPublicidad = publicidadService.guardarPublicidad(publicidad, photo);
        if (nuevaPublicidad != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPublicidad);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la publicidad.");
        }
    }

    @PutMapping("/actualizar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizarPublicidad(@PathVariable BigInteger id, @RequestBody Publicidad publicidad, @RequestPart(value = "photo", required = false) MultipartFile photo) {
        try {
            publicidadService.editarPublicidad(id, publicidad, photo);
            return ResponseEntity.ok("Publicación actualizada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe publicación con el ID: " + id);
        }
    }

    @DeleteMapping(path = "/eliminar/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> eliminarPublicidad(@PathVariable BigInteger id) {
        try {
            Optional<Publicidad> publiOptional = publicidadService.obtenerPublicidadPorId(id);
            if (!publiOptional.isPresent())  {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna Publicidad con ID especificado." + id);
            }

            publicidadService.eliminarPublicidad(id);

            return ResponseEntity.status(HttpStatus.OK).body("Publicidad eliminada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error al eliminar.");
        }
    }



}