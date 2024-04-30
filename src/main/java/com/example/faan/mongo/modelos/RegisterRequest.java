package com.example.faan.mongo.modelos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {


    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private String dni;
    private Role role;
    private  String direccion;
    private String telefono;
    private String email;
}
