package com.example.faan.mongo.Controller;

import com.example.faan.mongo.Service.PublicacionService;
import com.example.faan.mongo.modelos.Publicacion;
import com.example.faan.mongo.modelos.EnumsFijo.TipoPublicacion;
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Publicacion>> listarPublicacionesEncontradas() {
        List<Publicacion> publicacionesEncontradas = publicacionService.obtenerPublicacionesPorTipo(TipoPublicacion.ENCONTRADO);
        List<Publicacion> publicacionesEncontradasFiltradas = publicacionService.publicacionesConEstado(publicacionesEncontradas);
        return ResponseEntity.ok(publicacionesEncontradasFiltradas);
    }



    @PostMapping(path = "/guardarEncontrados")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> crearPublicacion(@RequestBody Publicacion publicacion) {
        try {
            if (publicacionService.obtenerPublicacionPorId(publicacion.getId()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("La publicación ya existe");
            }

            // Establecer el tipo de publicación como "ENCONTRADO" por defecto
            publicacion.setTipoPublicacion(TipoPublicacion.ENCONTRADO);
            publicacion.setEstadoRescatado(false);
            publicacion.setEstadoFavoritos(false);
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
    public ResponseEntity<String> actualizarPublicacionencontrados(@PathVariable BigInteger id, @Valid @RequestBody Publicacion publicacion) {
        try {
            // Verificar si la publicación existe y es del tipo "ENCONTRADO"
            Optional<Publicacion> publicacionEncontradaOptional = publicacionService.obtenerPublicacionPorId(id);
            if (!publicacionEncontradaOptional.isPresent() || !publicacionEncontradaOptional.get().getTipoPublicacion().equals(TipoPublicacion.ENCONTRADO)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La publicación no existe o no es del tipo ENCONTRADO");
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
            if (publicacion.getFoto() != null) {
            }
            publicacionExistente.setFoto(publicacion.getFoto());
            // Actualizar la publicación
            publicacionService.actualizarPublicacion(id, publicacionExistente);
            return ResponseEntity.ok("Publicación actualizada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error al actualizar la publicación");
        }
    }

    @DeleteMapping(path = "/eliminar/encontrados/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> eliminarPublicacionEncontrada(@PathVariable BigInteger id) {
        try {
            Optional<Publicacion> publicacionOptional = publicacionService.obtenerPublicacionPorId(id);
            if (!publicacionOptional.isPresent() || publicacionOptional.get().getTipoPublicacion() != TipoPublicacion.ENCONTRADO) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna publicación de tipo ENCONTRADO con el ID: " + id);
            }

            publicacionService.eliminarPublicacion(id);

            return ResponseEntity.status(HttpStatus.OK).body("Publicación de tipo ENCONTRADO eliminada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error al eliminar la publicación de tipo ENCONTRADO");
        }
    }

}



