package com.example.faan.mongo.modelos;

import com.example.faan.mongo.modelos.EnumsFijo.TipoAnimal;
import com.example.faan.mongo.modelos.EnumsFijo.TipoPublicacion;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "publicaciones")
public class Publicacion {

    @Id
    private BigInteger id;
    private String nombre;
    private String raza;
    private String sexo;
    private TipoAnimal tipoAnimal;
    private TipoPublicacion tipoPublicacion;
    private String descripcionEspecifica;
    private String fecha; // Cambiado a tipo String
    private String ubicacion;
    private Boolean estadoRescatado;
    private Boolean estadoFavoritos;

    private byte[] foto;
    private LocalDateTime fecha_publicacion;

    Usuario usuario;
}
