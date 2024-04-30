package com.example.faan.mongo.modelos;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "Publicaciones")
public class Publicacion  {
    @Id
    private BigInteger id;
    private String nombre;
    private String raza;
    private String sexo;
    private TipoAnimal tipoAnimal;
    private TipoPublicacion tipoPublicacion;
    private String descripcionEspecifica;
    private String fecha;
    private String ubicacion;
    private Boolean estadoRescatado;
    private byte[]  foto;


}
