package com.example.faan.mongo.Controller.primarys;

import com.example.faan.mongo.Service.PublicacionService;
import com.example.faan.mongo.modelos.EnumsFijo.Role;
import com.example.faan.mongo.modelos.Publicacion;
import com.example.faan.mongo.modelos.EnumsFijo.TipoPublicacion;
import com.example.faan.mongo.modelos.Usuario;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/publicaciones")
public class AdoptadosControllers {

    private final PublicacionService publicacionService;
    private final SimpMessagingTemplate messagingTemplate;

    public AdoptadosControllers(PublicacionService publicacionService, SimpMessagingTemplate messagingTemplate) {
        this.publicacionService = publicacionService;
        this.messagingTemplate = messagingTemplate;
    }

    ///METODO PARA LISTAR ADOPTADOS
    @GetMapping("/listar/adoptados")
    public ResponseEntity<List<Publicacion>> listarPublicacionesAdoptado() {
        List<Publicacion> publicacionesEncontradas = publicacionService.obtenerPublicacionesPorTipo(TipoPublicacion.ADOPCION);
        List<Publicacion> publicacionesEncontradasFiltradas = publicacionService.publicacionesConEstadoFalse(publicacionesEncontradas);
        return ResponseEntity.ok(publicacionesEncontradasFiltradas);
    }
    // funciona correctamente


    ///METODO PARA GUARDAR ADOPTADOS
    @PostMapping(path = "/guardarAdoptados")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<String> crearPublicacion(@RequestPart("publicacion") Publicacion publicacion, @RequestPart(value = "photo") MultipartFile photo) throws IOException {
        try {
          
            if (publicacionService.obtenerPublicacionPorId(publicacion.getId()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("La publicación ya existe.");
            }

            if (publicacionService.existePublicacionDuplicada(publicacion)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No puede duplicar exactamente la publicacion. Cambie al menos un dato");
            }

            // Establecer el tipo de publicación como "ENCONTRADO" por defecto
            publicacion.setTipoPublicacion(TipoPublicacion.ADOPCION);
            publicacion.setEstadoRescatado(false);
            publicacion.setEstadoFavoritos(false);
            Publicacion nuevaPublicacion = publicacionService.crearPublicacion(publicacion, photo);
            if (nuevaPublicacion != null) {
                messagingTemplate.convertAndSend("/topic/publicaciones", publicacion);
                return ResponseEntity.status(HttpStatus.CREATED).body("Publicación tipo 'Adopción' creada exitosamente.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Se produjo un error al crear la publicación.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al procesar la solicitud.");
        }
    }
    ////falta agregar validaciones, para que no se guarde el mismo animal mas de una vez


    ///METODO PARA ACTUALIZAR ADOPTADOS POR ID
    @PutMapping("/actualizarAdoptados/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> actualizarPublicacionadoptados(@PathVariable BigInteger id, @Valid @RequestBody Publicacion publicacion) {
        try {


            // Verificar si la publicación existe y es del tipo "ENCONTRADO"
            Optional<Publicacion> publicacionEncontradaOptional = publicacionService.obtenerPublicacionPorId(id);
            if (!publicacionEncontradaOptional.isPresent() || !publicacionEncontradaOptional.get().getTipoPublicacion().equals(TipoPublicacion.ADOPCION)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La publicación no existe o no es del tipo ADOPCIÓN.");
            }
            Publicacion publicacionExistente = publicacionEncontradaOptional.get();

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
    /// funciona correctamente.


    ///METODO PARA ELIMINAR ADOPCIONES POR ID
    @DeleteMapping(path = "/eliminar/adopciones/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminarPublicacionAdopcion(@PathVariable BigInteger id){
        try {

            Optional<Publicacion> publicacionOptional = publicacionService.obtenerPublicacionPorId(id);
            if (!publicacionOptional.isPresent() || publicacionOptional.get().getTipoPublicacion() != TipoPublicacion.ADOPCION) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró ninguna publicación de tipo ADOPCIÓN con el ID especificado." + id);
            }

            publicacionService.eliminarPublicacion(id);

            return ResponseEntity.status(HttpStatus.OK).body("Publicación de tipo ADOPCIÓN eliminada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Se produjo un error al eliminar la publicación de tipo ADOPCIÓN.");
        }
    }
    ///funciona correctamente

}
