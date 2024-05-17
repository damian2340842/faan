package com.example.faan.mongo.modelos.secundary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;
import java.time.LocalDateTime;

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

    @Field("diasPublicacion")
    private long diasPublicacion;

    @Field("eliminarPublicacion")
    private Boolean eliminarPublicacion;

    @Field("fechaCreacion")
    private LocalDateTime fechaCreacion;

    @Field("location")
    private Location location;

    // Enum para el estado de la notificación
    public enum EstadoNotificacion {
        ENVIADO,
        LEIDO,
        FINALIZADO
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Location {
        private double latitude;
        private double longitude;
    }
}
