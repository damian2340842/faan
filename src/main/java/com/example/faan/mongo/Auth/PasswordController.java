package com.example.faan.mongo.Auth;


import com.example.faan.mongo.Repository.UsuarioRepository;
import com.example.faan.mongo.modelos.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/auth/security")
public class PasswordController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String email, @RequestParam String username) {
        try {
            // Código para buscar y verificar el usuario
            Usuario usuario = usuarioRepository.findByEmailAndUsername(email, username);
            if (usuario == null) {
                throw new NoSuchElementException("Usuario no encontrado");
            }
            // Resto de la lógica para verificar el usuario y generar el token de verificación
            // ...

            return ResponseEntity.ok("Usuario verificado exitosamente");
        } catch (NoSuchElementException e) {
            // Manejo de excepciones NoSuchElementException
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } catch (Exception e) {
            // Manejo de otras excepciones
            System.out.println("Tipo de excepción: " + e.getClass().getSimpleName());
            System.out.println("Mensaje de excepción: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al verificar usuario");
        }
    }




//    @PostMapping("/update")
//    public ResponseEntity<String> updatePassword(@RequestParam String verificationToken, @RequestParam String newPassword) {
//        // Buscar usuario por token de verificación
//        Usuario usuario = usuarioRepository.findByVerificationToken(verificationToken);
//        if (usuario == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token de verificación inválido");
//        }
//
//        // Actualizar la contraseña del usuario
//        usuario.setPassword(passwordEncoder.encode(newPassword));
//        usuario.setVerificationToken(null);
//        usuarioRepository.save(usuario);
//
//        return ResponseEntity.ok("Contraseña actualizada exitosamente");
//    }

    @PostMapping("/update2")
    public ResponseEntity<String> updatePassword(@RequestParam String verificationToken, @RequestParam String newPassword) {
        try {
            // Buscar usuario por token de verificación
            Usuario usuario = usuarioRepository.findByVerificationToken(verificationToken);
            if (usuario == null) {
                throw new NoSuchElementException("Token de verificación inválido");
            }

            // Actualizar la contraseña del usuario
            usuario.setPassword(newPassword);
            usuario.setVerificationToken(null); // Limpiar el token de verificación
            usuarioRepository.save(usuario);

            return ResponseEntity.ok("Contraseña actualizada exitosamente");
        } catch (NoSuchElementException e) {
            // Manejo de excepciones
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token de verificación inválido");
        } catch (Exception e) {
            // Manejo de otras excepciones
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la contraseña");
        }
    }


    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
}
