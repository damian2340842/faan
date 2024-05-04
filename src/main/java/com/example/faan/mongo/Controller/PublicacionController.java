package com.example.faan.mongo.Controller;

import com.example.faan.mongo.Service.PublicacionService;
import com.example.faan.mongo.modelos.Publicacion;
import jakarta.validation.Valid;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/publicaciones")
public class PublicacionController {

    private final PublicacionService publicacionService;
    private final SimpMessagingTemplate messagingTemplate;

    public PublicacionController(PublicacionService publicacionService, SimpMessagingTemplate messagingTemplate) {
        this.publicacionService = publicacionService;
        this.messagingTemplate = messagingTemplate;
    }



    //LISTA RESCATADOS(ENCONTRADOS Y PERDIDOS)
    @GetMapping("/listar/Rescatados")
    public ResponseEntity<List<Publicacion>> listarPublicacionesRescatados() {
        List<Publicacion> publicacionesRescatadas = publicacionService.listarPublicacionesRescatadas();
        return ResponseEntity.ok(publicacionesRescatadas);
    }
    @GetMapping("/listar/Favoritos")
    public ResponseEntity<List<Publicacion>> listarPublicacionesFavoritos() {
        List<Publicacion> todasLasPublicaciones = publicacionService.obtenerTodasLasPublicaciones();
        List<Publicacion> publicacionesRescatadas = publicacionService.publicacionesConEstadoFavTrue(todasLasPublicaciones);
        return ResponseEntity.ok(publicacionesRescatadas);
    }



    //actualiza todos tipo de publicaciones
    @PutMapping("/actualizar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizarPublicacion(@PathVariable BigInteger id, @Valid @RequestBody Publicacion publicacion) {
        try {
            publicacionService.actualizarPublicacion(id, publicacion);
            return ResponseEntity.ok("Publicación actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error al actualizar la publicación");
        }
    }


//elimina todos tipo de publicaciones
    @DeleteMapping(path = "/eliminar/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> eliminarPublicacion(@PathVariable BigInteger id) {
        try {
            Optional<Publicacion> publicacionOptional = publicacionService.obtenerPublicacionPorId(id);
            if (!publicacionOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna publicación con el ID: " + id);
            }

            this.publicacionService.eliminarPublicacion(id);

            return ResponseEntity.status(HttpStatus.OK).body("Publicación eliminada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error al eliminar la publicación");
        }
    }



    @GetMapping("/buscar/{id}")
    public ResponseEntity<Publicacion> buscarPublicacionPorId(@PathVariable BigInteger id) {
        try {
            Publicacion publicacion = publicacionService.obtenerPublicacionPorId(id)
                    .orElse(null);
            if(publicacion != null) {
                return ResponseEntity.ok(publicacion);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    //filtrado por nombre,raza,fecha




}