package com.example.faan.mongo.Controller.primarys;

import com.example.faan.mongo.Repository.UsuarioRepository;
import com.example.faan.mongo.Service.AuthService;
import com.example.faan.mongo.Service.CounterService;
import com.example.faan.mongo.Service.Mi_UbicacionService;
import com.example.faan.mongo.Service.UsuarioService;
import com.example.faan.mongo.Validaciones.Validacion_Usuario;
import com.example.faan.mongo.modelos.AuthResponse;
import com.example.faan.mongo.modelos.LoginRequest;
import com.example.faan.mongo.modelos.Usuario;
import com.example.faan.mongo.modelos.secundary.Mi_Ubicacion;
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
    private final UsuarioService usuarioService;

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
    //Cerrar Secion OK
    @PostMapping("/signout")
    public ResponseEntity<String> signOut(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return ResponseEntity.ok("Sesión cerrada correctamente.");
    }

    //ubi

    private final Mi_UbicacionService mi_UbicacionService;

    @PostMapping("/guardar")
    public ResponseEntity<Mi_Ubicacion> guardarUbicacion(@RequestParam double latitude, @RequestParam double longitude) {
        try {
            Mi_Ubicacion nuevaUbicacion = mi_UbicacionService.guardarUbicacion(latitude, longitude);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaUbicacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}




//    @PostMapping("/actualizarUbi")
//    public ResponseEntity<String> actualizarUbicacion(@RequestBody Mi_Ubicacion ubicacion) {
//        try {
//            // Verificar si la ubicación ya existe en la base de datos para el usuario dado
//            Usuario usuario = ubicacion.getUserId();
//            if (usuario != null) {
//                Mi_Ubicacion ubicacionExistente = ubicacionService.obtenerUbicacionPorUsuario(usuario);
//                if (ubicacionExistente != null) {
//                    // Si la ubicación ya existe, actualiza sus valores
//                    ubicacionExistente.setLatitude(ubicacion.getLatitude());
//                    ubicacionExistente.setLongitude(ubicacion.getLongitude());
//                    ubicacionService.actualizarUbicacionUsuario(ubicacionExistente);
//                    return ResponseEntity.ok("Ubicación actualizada correctamente.");
//                } else {
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró una ubicación para este usuario.");
//                }
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El campo 'userId' es obligatorio.");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la ubicación del usuario.");
//        }
//    }




