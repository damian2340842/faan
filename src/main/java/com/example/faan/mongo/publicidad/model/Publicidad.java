package com.example.faan.mongo.publicidad.model;


import com.example.faan.mongo.file.model.entity.Photo;
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
@Document(collection = "publicidad")
public class Publicidad {

    @Id
    private BigInteger id;
    private String titulo;
    private String descripcion;
    private Photo photoPublicidad;
}
