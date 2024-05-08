package com.example.faan.mongo.Controller.primarys;


import com.example.faan.mongo.Service.AuthService;
import com.example.faan.mongo.Service.UsuarioService;
import com.example.faan.mongo.modelos.EnumsFijo.TipoPublicacion;
import com.example.faan.mongo.modelos.Publicacion;
import com.example.faan.mongo.modelos.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsersController {

    @Autowired
    private UsuarioService usuarioService;

    public UsersController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    //METODO PARA ACTUALIZAR LOS DATOS DE USUARIOS
    @PutMapping("/actualizarUser/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable String id, @Valid @RequestBody Usuario usuario) {
        try {

            Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorId(id);
            if (!usuarioOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("El usuario con el ID proporcionado no existe: " + id);
            }
            Usuario usuarioExistente = usuarioOptional.get();

            if (usuario.getNombre() != null) {
                usuarioExistente.setNombre(usuario.getNombre());
            }
            if (usuario.getApellido() != null) {
                usuarioExistente.setApellido(usuario.getApellido());
            }
            if (usuario.getUsername() != null) {
                usuarioExistente.setUsername(usuario.getUsername());
            }
            if (usuario.getPassword() != null) {
                usuarioExistente.setPassword(usuario.getPassword());
            }
            if (usuario.getDireccion() != null) {
                usuarioExistente.setDireccion(usuario.getDireccion());
            }
            if (usuario.getTelefono() != null) {
                usuarioExistente.setTelefono(usuario.getTelefono());
            }
            if (usuario.getEmail() != null) {
                usuarioExistente.setEmail(usuario.getEmail());
            }

            if (usuario.getRole() == null) {
                usuario.setRole(usuarioExistente.getRole());
            }

            usuarioService.actualizarUsuario(id, usuarioExistente);

            return ResponseEntity.ok("Usuario actualizado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Se produjo un error al actualizar el usuario.");
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/obtenerUser/{username}")
    public ResponseEntity<Usuario> obtenerUsuarioPorUsername(@PathVariable String username) {
        Usuario usuarioOptional = usuarioService.findByUsername(username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(usuarioOptional);
    }

    //METODO PARA ELIMINAR USUARIOS ---- SOLO ADMIN
    @DeleteMapping("/eliminarUser/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable("id") String id) {
        try {
            // Verificar si el usuario tiene el rol de administrador
            if (!SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ADMIN"))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("El usuario no tiene el rol de administrador, por lo que no puede realizar la eliminación de usuarios.");
            }

            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            // usuario autenticado está intentando eliminarse a sí mismo
            Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuarioPorId(id);
            if (!usuarioOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("El usuario con el ID proporcionado no existe: " + id);
            }
            Usuario usuario = usuarioOptional.get();
            if (usuario.getUsername().equals(username)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No puede eliminarse el ADMIN.");
            }

            usuarioService.eliminarUsuario(id);

            return ResponseEntity.status(HttpStatus.OK).body("Usuario eliminado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ups! Ocurrió un error al eliminar el usuario.");
        }
    }
}
