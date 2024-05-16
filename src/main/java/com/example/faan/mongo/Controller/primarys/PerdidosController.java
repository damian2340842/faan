package com.example.faan.mongo.Controller.primarys;

import com.example.faan.mongo.Service.PublicacionService;
import com.example.faan.mongo.modelos.EnumsFijo.TipoPublicacion;
import com.example.faan.mongo.modelos.Publicacion;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/publicaciones")
public class PerdidosController {

    private final PublicacionService publicacionService;
    private final SimpMessagingTemplate messagingTemplate;

    public PerdidosController(PublicacionService publicacionService, SimpMessagingTemplate messagingTemplate) {
        this.publicacionService = publicacionService;
        this.messagingTemplate = messagingTemplate;
    }

/// METODO PARA GUARDAR ANIMALES PERDIDOS
    //Perdidos OK
    @PostMapping(path = "/guardarPerdidos")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> crearPublicacion(@RequestPart("publicacion") Publicacion publicacion, @RequestPart("photo") MultipartFile photo) throws IOException {
        try {
            if (publicacionService.obtenerPublicacionPorId(publicacion.getId()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("La publicación ya existe.");
            }

            if (publicacionService.existePublicacionDuplicada(publicacion)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No puede duplicar exactamente la publicacion. Cambie al menos un dato");
            }

            publicacion.setTipoPublicacion(TipoPublicacion.PERDIDO);

            Publicacion nuevaPublicacion = publicacionService.crearPublicacion(publicacion, photo);
            if (nuevaPublicacion != null) {
                messagingTemplate.convertAndSend("/topic/publicaciones", publicacion);
                return ResponseEntity.status(HttpStatus.CREATED).body("Publicación de tipo perdido creada exitosamente.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Se produjo un error al crear la publicación.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error al procesar la solicitud.");
        }
    }
    ///agregar validaciones, para que no permita guardar la misma publicacion dos veces.


// METODO PARA ENLISTAR LOS ANMALES EN ESTADO DE PERDIDO
    @GetMapping("/listar/perdidos")
    public ResponseEntity<List<Publicacion>> listarPublicacionesPerdidas() {
        List<Publicacion> publicacionesPerdidas = publicacionService.obtenerPublicacionesPorTipo(TipoPublicacion.PERDIDO);
        List<Publicacion> publicacionesPerdidasFiltradas = publicacionService.publicacionesConEstadoTrue(publicacionesPerdidas);
        return ResponseEntity.ok(publicacionesPerdidasFiltradas);
    }
    // funciona correctamente.


    ///METODO PARA ACTUALIZAR PERDIDOS CON EL ID
    @PutMapping("/actualizarPerdidos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizarPublicacionPerdidos(@PathVariable BigInteger id, @Valid @RequestBody Publicacion publicacion) {
        try {
            // Verificar si la publicación existe y es del tipo "ENCONTRADO"
            Optional<Publicacion> publicacionEncontradaOptional = publicacionService.obtenerPublicacionPorId(id);
            if (!publicacionEncontradaOptional.isPresent() || !publicacionEncontradaOptional.get().getTipoPublicacion().equals(TipoPublicacion.PERDIDO)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La publicación no existe o no es del tipo perdido.");
            }
            Publicacion publicacionExistente = publicacionEncontradaOptional.get();

            // Fusionar los campos del cuerpo de la solicitud con los campos existentes de la publicación
            if (publicacion.getNombre() != null) {
                publicacionExistente.setNombre(publicacion.getNombre());
            }
            if (publicacion.getRaza() != null) {
                publicacionExistente.setRaza(publicacion.getRaza());
            }
            if (publicacion.getSexo() != null) {
                publicacionExistente.setSexo(publicacion.getSexo());
            }
            if (publicacion.getTipoAnimal() != null) {
                publicacionExistente.setTipoAnimal(publicacion.getTipoAnimal());
            }
            if (publicacion.getTipoPublicacion() != null) {
                publicacionExistente.setTipoPublicacion(publicacion.getTipoPublicacion());
            }
            if (publicacion.getDescripcionEspecifica() != null) {
                publicacionExistente.setDescripcionEspecifica(publicacion.getDescripcionEspecifica());
            }
            if (publicacion.getFecha() != null) {
                publicacionExistente.setFecha(publicacion.getFecha());
            }
            if (publicacion.getUbicacion() != null) {
                publicacionExistente.setUbicacion(publicacion.getUbicacion());
            }
            Boolean estadoRescatado = publicacion.getEstadoRescatado();
            if (estadoRescatado != null) {
                publicacionExistente.setEstadoRescatado(estadoRescatado);
            }
            Boolean estadoFavoritos = publicacion.getEstadoFavoritos();
            if (estadoFavoritos != null) {
                publicacionExistente.setEstadoFavoritos(publicacion.getEstadoFavoritos());
            }
            // Actualizar la publicación
            publicacionService.actualizarPublicacion(id, publicacionExistente);
            return ResponseEntity.ok("Publicación actualizada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Se produjo un error al actualizar la publicación.");
        }
    }
    //funciona correctamente

    ///METODO PARA ELIMINAR PUBLICACION DE TIPO
    @DeleteMapping(path = "/eliminar/perdidos/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> eliminarPublicacionPerdido(@PathVariable BigInteger id) {
        try {
            Optional<Publicacion> publicacionOptional = publicacionService.obtenerPublicacionPorId(id);
            if (!publicacionOptional.isPresent() || publicacionOptional.get().getTipoPublicacion() != TipoPublicacion.PERDIDO) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna publicación de tipo PERDIDO con el ID especificado." + id);
            }

            publicacionService.eliminarPublicacion(id);

            return ResponseEntity.status(HttpStatus.OK).body("Publicación de tipo PERDIDO eliminada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error al eliminar la publicación de tipo PERDIDO.");
        }
    }
    /// funciona correctamente


}
