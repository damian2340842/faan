package com.example.faan.mongo.Controller.primarys;

import com.example.faan.mongo.Repository.UsuarioRepository;
import com.example.faan.mongo.Service.AuthService;
import com.example.faan.mongo.Service.CounterService;
import com.example.faan.mongo.Validaciones.Validacion_Usuario;
import com.example.faan.mongo.modelos.AuthResponse;
import com.example.faan.mongo.modelos.LoginRequest;
import com.example.faan.mongo.modelos.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200", "http://10.0.2.2:8080"})
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;
    private final CounterService counterService;
    private final Validacion_Usuario validacionUsuario;

    /// METODO PARA INICIAR SESIÓN
    //Login :) OK
    @PostMapping("/signin")
    public ResponseEntity<Map<String, String>> movilSignIn(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.login(loginRequest);
            Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

            if (authResponse == null || authResponse.getToken() == null) {
                throw new RuntimeException("Error al iniciar sesión.");
            }

            Map<String, String> response = new HashMap<>();
            response.put("token", authResponse.getToken());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Credenciales incorrectas"));
        }
    }

    /// METODO PARA SALIR
    //Cerrar Seccion OK
    @PostMapping("/signout")
    public ResponseEntity<String> signOut(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return ResponseEntity.ok("Sesión cerrada correctamente.");
    }

}
