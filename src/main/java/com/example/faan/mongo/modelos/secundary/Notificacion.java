package com.example.faan.mongo.modelos.secundary;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "notificaciones")
public class Notificacion {

    @Id
    private String _id;

    @Field("fullNameAnimal")
    private String fullNameAnimal;

    @Field("cuerpoMensaje")
    private String cuerpoMensaje;

    @Field("estadoMensaje")
    private String estadoMensaje;

    @Field("diasPublicacion") // Corrección de tipeo
    private String diasPublicacion;

    @Field("eliminarPublicacion")
    private String eliminarPublicacion; // Corrección de tipeo
}
