package com.example.faan.mongo.modelos;

import com.example.faan.mongo.file.model.entity.Photo;
import com.example.faan.mongo.modelos.EnumsFijo.TipoAnimal;
import com.example.faan.mongo.modelos.EnumsFijo.TipoPublicacion;
import com.example.faan.mongo.modelos.secundary.Location;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
    private Location location;
    private Boolean estadoRescatado;
    private Boolean estadoFavoritos;

    @DBRef
    private Photo photo;
    private LocalDateTime fecha_publicacion;

    @DBRef
    private Usuario usuario;
}
