package com.example.faan.mongo.Controller;

import com.example.faan.mongo.Repository.UsuarioRepository;
import com.example.faan.mongo.Service.AuthService;
import com.example.faan.mongo.Service.CounterService;
import com.example.faan.mongo.modelos.AuthResponse;
import com.example.faan.mongo.modelos.LoginRequest;
import com.example.faan.mongo.modelos.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200", "http://10.0.2.2:8080"})
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
        private final UsuarioRepository usuarioRepository;

    private final CounterService counterService;



    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> movilSignIn(@RequestBody LoginRequest loginRequest) {
        try{
        AuthResponse authResponse = authService.login(loginRequest);
        Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("usuario not found"));

            if (authResponse == null) {
                throw new RuntimeException("Error al iniciar sesión");
            }
        authResponse.setUsuario(usuario);
        return ResponseEntity.ok(authResponse);
        }catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Credenciales incorrectas", null));
        }
    }

    @PostMapping("/register")
    private ResponseEntity<AuthResponse> register(@RequestBody Usuario usuario) {
        try {
            String username = usuario.getUsername();
            String email = usuario.getEmail();

            if (authService.isusernameAlreadyRegistered(username)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponse("El nombre de usuario ya está registrado", null));
            }

            return ResponseEntity.ok(authService.register(usuario));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse("Error en el registro", null));
        }
    }




    @PostMapping("/signout")
    public ResponseEntity<String> signOut(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return ResponseEntity.ok("Sesión cerrada correctamente.");
}
}