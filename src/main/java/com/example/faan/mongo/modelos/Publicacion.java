package com.example.faan.mongo.modelos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Publicaciones")
public class Publicacion  {
    @Id
    private Long id;
    private String nombre;
    private String raza;
    private String sexo;
    private String descripcionEspecifica;
    private String Fecha;
    private String Ubicacion;
    private String Foto;



}
