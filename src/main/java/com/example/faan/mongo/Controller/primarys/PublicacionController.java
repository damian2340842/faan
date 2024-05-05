package com.example.faan.mongo.Controller.primarys;

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


    //METODO PARA LISTAR PUBLICACIONES GENERAL
    @GetMapping("/listar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Publicacion>> listarPublicaciones() {
        try {
            List<Publicacion> publicaciones = publicacionService.obtenerTodasLasPublicaciones();
            return ResponseEntity.ok(publicaciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    ///funciona correctamente



    // METODO PARA LA LISTA RESCATADOS(ENCONTRADOS Y PERDIDOS)
    @GetMapping("/listar/Rescatados")
    public ResponseEntity<?> listarPublicacionesRescatados() {
        List<Publicacion> publicacionesRescatadas = publicacionService.listarPublicacionesRescatadas();
        if (publicacionesRescatadas.isEmpty()) {
            // No se encontraron publicaciones, se puede retornar un mensaje de error o un status 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron publicaciones rescatadasw.");
        }
        return ResponseEntity.ok(publicacionesRescatadas);
    }


    // METODO PARA LA LISTA RESCATADOS(Rescatados y favoritos)
    @GetMapping("/listar/Favoritos")
    public ResponseEntity<?> listarPublicacionesFavoritos() {
        List<Publicacion> todasLasPublicaciones = publicacionService.obtenerTodasLasPublicaciones();
        List<Publicacion> publicacionesRescatadas = publicacionService.publicacionesConEstadoFavTrue(todasLasPublicaciones);

        if (publicacionesRescatadas.isEmpty()) {
            // No se encontraron publicaciones favoritas, se puede retornar un mensaje de error o un status 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron publicaciones favoritas.");
        }

        return ResponseEntity.ok(publicacionesRescatadas);
    }



    //GUARDAR PUBLICACIONES EN GENERAL
    @PostMapping(path = "/guardarPublicaciones")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> crearPublicacion(@RequestBody Publicacion publicacion) {
        try {
            if (publicacionService.obtenerPublicacionPorId(publicacion.getId()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("La publicación ya existe");
            }

            Publicacion nuevaPublicacion = publicacionService.crearPublicacion(publicacion);
            if (nuevaPublicacion != null) {
                messagingTemplate.convertAndSend("/topic/publicaciones", publicacion);

                return ResponseEntity.status(HttpStatus.CREATED).body("Publicación creada exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error al crear la publicación");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error al procesar la solicitud");
        }
    }
    ///funciona correctamente


    //METODO PARA ACTUALIZAR TODO TIPO DE PUBLICACION

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
    //funciona correctamente


//METODO PARA ELIMINAR POR ID
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
    ///funciona correctamente


///METODO PARA BUSCAR POR ID
@GetMapping("/buscar/{id}")
public ResponseEntity<?> buscarPublicacionPorId(@PathVariable BigInteger id) {
    try {
        Optional<Publicacion> publicacionOpt = publicacionService.obtenerPublicacionPorId(id);
        if (publicacionOpt.isPresent()) {
            return ResponseEntity.ok(publicacionOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la publicación con el ID seleccionado.");
        }
    } catch (Exception e) {
        // Optionally, you can include a more descriptive message or log the error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

    ///funciona correctamente


    //filtrado por nombre,raza,fecha




}