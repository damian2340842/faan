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

import java.util.UUID;

@RestController
@RequestMapping("/auth/security")
public class PasswordController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam String dni, @RequestParam String username) {
        // Buscar usuario por DNI y nombre de usuario
        Usuario usuario = usuarioRepository.findByDniAndUsername(dni, username);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        String verificationToken = generateVerificationToken();
        usuario.setVerificationToken(verificationToken);
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("vtoken:" + verificationToken);
    }

    @PostMapping("/update")
    public ResponseEntity<String> updatePassword(@RequestParam String verificationToken, @RequestParam String newPassword) {
        // Buscar usuario por token de verificación
        Usuario usuario = usuarioRepository.findByVerificationToken(verificationToken);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token de verificación inválido");
        }

        // Actualizar la contraseña del usuario
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuario.setVerificationToken(null);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Contraseña actualizada exitosamente");
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
}
