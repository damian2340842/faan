package com.example.faan.mongo.Controller;

import com.example.faan.mongo.Service.PublicacionService;
import com.example.faan.mongo.modelos.Publicacion;
import com.example.faan.mongo.modelos.TipoPublicacion;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/publicaciones")
public class EncontradosController {
    private final PublicacionService publicacionService;
    private final SimpMessagingTemplate messagingTemplate;

    public EncontradosController(PublicacionService publicacionService, SimpMessagingTemplate messagingTemplate) {
        this.publicacionService = publicacionService;
        this.messagingTemplate = messagingTemplate;
    }
    @GetMapping("/listar/encontradas")
    public ResponseEntity<List<Publicacion>> listarPublicacionesEncontradas() {
        List<Publicacion> publicacionesEncontradas = publicacionService.obtenerPublicacionesPorTipo(TipoPublicacion.ENCONTRADO);
        List<Publicacion> publicacionesEncontradasFiltradas = publicacionService.publicacionesConEstadoFalse(publicacionesEncontradas);
        return ResponseEntity.ok(publicacionesEncontradasFiltradas);
    }
    @GetMapping("/listar/Rescatados")
    public ResponseEntity<List<Publicacion>> listarPublicacionesRescatados() {
        List<Publicacion> publicacionesEncontradas = publicacionService.obtenerPublicacionesPorTipo(TipoPublicacion.ENCONTRADO);
        List<Publicacion> publicacionesEncontradasFiltradasR = publicacionService.publicacionesConEstadoTrue(publicacionesEncontradas);
        return ResponseEntity.ok(publicacionesEncontradasFiltradasR);
    }

    @PostMapping(path = "/guardarPublicacionesEncontrados")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> crearPublicacion(@RequestBody Publicacion publicacion) {
        try {
            if (publicacionService.obtenerPublicacionPorId(publicacion.getId()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("La publicación ya existe");
            }

            // Establecer el tipo de publicación como "ENCONTRADO" por defecto
            publicacion.setTipoPublicacion(TipoPublicacion.ENCONTRADO);

            Publicacion nuevaPublicacion = publicacionService.crearPublicacion(publicacion);
            if (nuevaPublicacion != null) {
                messagingTemplate.convertAndSend("/topic/publicaciones", publicacion);
                return ResponseEntity.status(HttpStatus.CREATED).body("Publicación tipo encontrado creada exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error al crear la publicación");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error al procesar la solicitud");
        }
    }

    @PutMapping("/actualizarEncontradas/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizarPublicacion(@PathVariable BigInteger id, @Valid @RequestBody Publicacion publicacion) {
        try {
            publicacionService.actualizarPublicacion(id, publicacion);
            return ResponseEntity.ok("Publicación actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error al actualizar la publicación");
        }
    }



    @DeleteMapping(path = "/eliminarEncontradas/{id}")
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



    @GetMapping("/buscarEncontradas/{id}")
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
}



