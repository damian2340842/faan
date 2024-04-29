package com.example.faan.mongo.modelos;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
    private byte[]  Foto;


}
