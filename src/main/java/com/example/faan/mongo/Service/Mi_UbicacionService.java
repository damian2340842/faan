package com.example.faan.mongo.Service;

import com.example.faan.mongo.Repository.secundary.Mi_UbicacionRepository;
import com.example.faan.mongo.modelos.Usuario;
import com.example.faan.mongo.modelos.secundary.Mi_Ubicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class Mi_UbicacionService {

    private final Mi_UbicacionRepository mi_UbicacionRepository;
    private final UsuarioService usuarioService;

    @Autowired
    public Mi_UbicacionService(Mi_UbicacionRepository mi_UbicacionRepository, UsuarioService usuarioService) {
        this.mi_UbicacionRepository = mi_UbicacionRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public Mi_Ubicacion guardarUbicacion(Mi_Ubicacion ubicacion) {
        // Validar coordenadas
        if (ubicacion.getLatitude() < -90 || ubicacion.getLatitude() > 90) {
            throw new IllegalArgumentException("Latitud inválida: debe estar entre -90 y 90.");
        }
        if (ubicacion.getLongitude() < -180 || ubicacion.getLongitude() > 180) {
            throw new IllegalArgumentException("Longitud inválida: debe estar entre -180 y 180.");
        }

        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.findByUsername(username);

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado: " + username);
        }

        // Establecer el usuario en la ubicación
        ubicacion.setUserId(usuario);

        // Guardar la ubicación en la base de datos
        return mi_UbicacionRepository.save(ubicacion);
    }

    public List<Mi_Ubicacion> obtenerTodasLasUbicaciones() {
        return mi_UbicacionRepository.findAll();
    }

}