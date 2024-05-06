package com.example.faan.mongo.Validaciones;

import com.example.faan.mongo.Repository.UsuarioRepository;
import org.springframework.stereotype.Component;

@Component
public class Validacion_Usuario {

    private final UsuarioRepository userRepository;

    public Validacion_Usuario(UsuarioRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String validateNombre(String fieldName, String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return fieldName + " es requerido";
        }
        if (!nombre.matches("^[a-zA-Z]+$")) {
            return fieldName + " solo puede contener letras";
        }
        // Convertir la primera letra a mayúscula
        nombre = nombre.substring(0, 1).toUpperCase() + nombre.substring(1);

        return null; // validación es exitosa
    }

    public String validateApellido(String fieldName, String apellido) {
        if (apellido == null || apellido.isEmpty()) {
            return fieldName + " es requerido";
        }
        if (!apellido.matches("^[a-zA-Z]+$")) {
            return fieldName + " solo puede contener letras";
        }
        // Convertir la primera letra a mayúscula
        apellido = apellido.substring(0, 1).toUpperCase() + apellido.substring(1);

        return null; // validación es exitosa
    }

    public String validateUsername(String fieldName, String username) {
        return validateNombre(fieldName, username);
    }

    public String validatePassword(String fieldName, String password) {
        // Verificar la longitud mínima
        if (password.length() < 8) {
            return fieldName + " debe tener al menos 8 caracteres";
        }

        // Al menos una letra minúscula
        if (!password.matches(".*[a-z].*")) {
            return fieldName + " debe contener al menos una letra minúscula";
        }

        // Al menos una letra mayúscula
        if (!password.matches(".*[A-Z].*")) {
            return fieldName + " debe contener al menos una letra mayúscula";
        }

        // Al menos un dígito
        if (!password.matches(".*\\d.*")) {
            return fieldName + " debe contener al menos un dígito";
        }

        // Al menos un carácter especial
        if (!password.matches(".*[@#$%&].*")) {
            return fieldName + " debe contener al menos un carácter especial";
        }

        return null; // La validación es exitosa, no hay mensajes de error
    }

    public String validateDireccion(String fieldName, String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            return fieldName + " no puede estar vacía";
        }
        ////////////////////////////////////
        return null; // validación es exitosa
    }

    public String validateTelefono(String fieldName, String telefono) {
        if (telefono == null || telefono.length() != 10) {
            return fieldName + " debe tener exactamente 10 dígitos";
        }

        if (!telefono.matches("\\d+")) {
            return fieldName + " debe contener solo dígitos";
        }

        if (!telefono.startsWith("0")) {
            return fieldName + " debe empezar con 0";
        }

        return null; // validación es exitosa
    }

    public String validateEmail(String email) {
        if (email == null || !email.matches("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b")) {
            return "Correo electrónico no válido";
        }

        return null; // validación es exitosa
    }

}