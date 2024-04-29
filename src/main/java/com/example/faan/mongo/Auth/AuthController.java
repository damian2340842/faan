package com.example.faan.mongo.Auth;

import com.example.faan.mongo.Repository.UsuarioRepository;
import com.example.faan.mongo.modelos.AuthResponse;
import com.example.faan.mongo.modelos.LoginRequest;
import com.example.faan.mongo.modelos.RegisterRequest;
import com.example.faan.mongo.modelos.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200", "http://10.0.2.2:8080"})
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
        private final UsuarioRepository usuarioRepository;
///opp

//    @PostMapping("/v1/signin")
//    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest1) {
//
//        AuthResponse authResponse = authService.loginAdmin(loginRequest1);
//        Usuario usuario = usuarioRepository.findByUsername(loginRequest1.getUsername())
//                .orElseThrow(() -> new RuntimeException("Usuario not found"));
//
//        authResponse.setUsuario(usuario);
//
//        return ResponseEntity.ok(authResponse);
//
//    }
//
//    @PostMapping("/v0/signin")
//    public ResponseEntity<AuthResponse> v0signIn(@RequestBody LoginRequest loginRequest) {
//        try {
//            return ResponseEntity.ok(authService.loginAdmin(loginRequest));
//        } catch (Exception e) {
//            // Manejo de excepciones
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse("Error en el servidor", null));
//        }
//    }


    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> movilSignIn(@RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.login(loginRequest);
        Usuario usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("usuario not found"));

        authResponse.setUsuario(usuario);

        return ResponseEntity.ok(authResponse);
    }


    @PostMapping("/signin2")
    public ResponseEntity<AuthResponse> movilSignIn2(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.login(loginRequest);
            String username = loginRequest.getUsername();
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario not found"));

            if (authResponse == null) {
                throw new RuntimeException("Error al iniciar sesión");
            }

            authResponse.setUsuario(usuario);

            return ResponseEntity.ok(authResponse);
        }catch (Exception e) {
            // Manejo de otras excepciones
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Credenciales incorrectas", null));
        }
    }



    @PostMapping(value = "/register")
    private ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            String username = request.getUsername();
            String password = request.getPassword();

            if (username == null || username.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("El nombre de usuario no puede estar vacío", null));
            }

            if (password == null || password.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("La contraseña no puede estar vacía", null));
            }

            if (authService.isusernameAlreadyRegistered(username)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Usuario " + username + " ya registrado", null));
            }

            return ResponseEntity.ok(authService.register(request));
        } catch (Exception e) {
            // Manejo de excepciones
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse("Error en el registro", null));
        }
    }



    @PostMapping("/signout")
    public ResponseEntity<String> signOut(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return ResponseEntity.ok("Sesión cerrada correctamente.");
}
}