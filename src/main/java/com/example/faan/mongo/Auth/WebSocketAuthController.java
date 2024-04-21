package com.example.faan.mongo.Auth;

import com.example.faan.mongo.Auth.AuthService;
import com.example.faan.mongo.Repository.UsuarioRepository;
import com.example.faan.mongo.modelos.AuthResponse;
import com.example.faan.mongo.modelos.LoginRequest;
import com.example.faan.mongo.modelos.RegisterRequest;
import com.example.faan.mongo.modelos.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketAuthController {
    public boolean isUsernameAlreadyRegistered(String username) {
        return usuarioRepository.findByUsername(username).isPresent();
    }
    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Asegúrate de que esta importación sea correcta

    @MessageMapping("/auth/signin")
    @SendTo("/topic/auth/login")
    public ResponseEntity<AuthResponse> signIn(LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario not found"));

        authResponse.setUsuario(usuario);
        return ResponseEntity.ok(authResponse);
    }

    @MessageMapping("/auth/register")
    @SendTo("/topic/auth/register")
    public ResponseEntity<AuthResponse> register(RegisterRequest registerRequest) {
        if (authService.isusernameAlreadyRegistered(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new AuthResponse("El usuario " + registerRequest.getUsername() + " ya está registrado", null));
        }
        return ResponseEntity.ok(authService.register(registerRequest));
    }
}
