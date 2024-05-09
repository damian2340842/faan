package com.example.faan.mongo.Controller.primarys;

import com.example.faan.mongo.Service.PublicacionService;
import com.example.faan.mongo.modelos.Publicacion;
import com.example.faan.mongo.modelos.Usuario;
import jakarta.validation.Valid;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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


    //LISTAR TODAS MIS PUBLICACIONES SUBIDAS
    @GetMapping("/MisPublicaciones")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Publicacion>> listarPublicacionesM() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Obtener todas las publicaciones asociadas al usuario autenticado
            List<Publicacion> publicaciones = publicacionService.obtenerTodasLasPublicacionesPorUsuario(username);

            // Recorrer cada publicación para establecer el nombre y apellido del usuario
            for (Publicacion publicacion : publicaciones) {
                Usuario usuario = publicacion.getUsuario();
                if (usuario != null) {
                    Usuario usuarioReducido = new Usuario();
                    usuarioReducido.setNombre(usuario.getNombre());
                    usuarioReducido.setApellido(usuario.getApellido());
                    publicacion.setUsuario(usuarioReducido);
                }
            }

            return ResponseEntity.ok(publicaciones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }





    //GUARDAR PUBLICACIONES EN GENERAL ----- SOLO PARA PRUEBAS!!
    @PostMapping(path = "/guardarPublicaciones")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> crearPublicacion(@RequestPart("publicacion") Publicacion publicacion, @RequestPart(value = "photo", required = false) MultipartFile photo) throws IOException {

            if (publicacionService.obtenerPublicacionPorId(publicacion.getId()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("La publicación ya existe");
            }

            if (publicacionService.existePublicacionDuplicada(publicacion)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No puede duplicar exactamente la publicacion. Cambie al menos un dato");
            }


            Publicacion nuevaPublicacion = publicacionService.crearPublicacion(publicacion, photo);
            if (nuevaPublicacion != null) {
                messagingTemplate.convertAndSend("/topic/publicaciones", publicacion);

                return ResponseEntity.status(HttpStatus.CREATED).body("Publicación creada exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hubo un error al crear la publicación");
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
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

    ///funciona correctamente


    //BUSQUEDA POR NOMBRE, RAZA, FECHA
    @GetMapping("/buscar")
    public ResponseEntity<List<Publicacion>> buscarPublicaciones(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String raza,
            @RequestParam(required = false) String fecha
            ){
        List<Publicacion> publicaciones = publicacionService.buscarPublicaciones(nombre, raza, fecha);
        return ResponseEntity.ok(publicaciones);
    }



}