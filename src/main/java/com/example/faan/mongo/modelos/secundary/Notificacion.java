package com.example.faan.mongo.modelos.secundary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "notificaciones")
public class Notificacion {

    @Id
    private BigInteger id;

    @Field("fullNameAnimal")
    private String fullNameAnimal;

    @Field("cuerpoMensaje")
    private String cuerpoMensaje;

    @Field("estadoMensaje")
    private EstadoNotificacion estadoMensaje;

    @Field("diasPublicacion") // Corrección de tipeo
    private String diasPublicacion;

    @Field("eliminarPublicacion")
    private Boolean eliminarPublicacion; // Corrección de tipeo

    public enum EstadoNotificacion {
        ENVIADO,
        LEIDO,
        FINALIZADO
    }
}
