package com.example.faan.mongo.Service;

import com.example.faan.mongo.Repository.UsuarioRepository;
import com.example.faan.mongo.jwt.JwtService;
import com.example.faan.mongo.modelos.AuthResponse;
import com.example.faan.mongo.modelos.EnumsFijo.Role;
import com.example.faan.mongo.modelos.LoginRequest;
import com.example.faan.mongo.modelos.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.faan.mongo.modelos.AuthResponse;


import java.math.BigInteger;
import java.util.Optional;

import static com.example.faan.mongo.modelos.EnumsFijo.Role.USER;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final CounterService counterService;

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        UserDetails userDetails=usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow();
        String token =jwtService.getToken(userDetails);
        return AuthResponse.builder()
                .token(token)
        .build();
    }


    public AuthResponse register(Usuario request) {
        BigInteger newUserId = counterService.getNextSequence("usuario_id");

        // Verificar si ya existe un usuario con el rol ADMIN
        boolean isAdminAlreadyRegistered = usuarioRepository.existsByRole(Role.ADMIN);
        // Si ya hay un ADMIN registrado, cambiar el rol a USER
        Role role = isAdminAlreadyRegistered ? Role.USER : Role.ADMIN;

        Usuario usuario = Usuario.builder()
                .id(newUserId)
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .role(request.getRole())
                .role(role)
                .email(request.getEmail())
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .build();
        usuarioRepository.save(usuario);

        String token = jwtService.getToken(usuario); // Obtener el token JWT

        // Crear un objeto AuthResponseDTO con el token y el nombre de usuario
        return AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername()) // Usar el método getter generado por Lombok
                .build();
    }



        public boolean isusernameAlreadyRegistered(String username) {
        Optional<Usuario> userOptional = usuarioRepository.findByUsername(username);
        return userOptional.isPresent();
    }


}
