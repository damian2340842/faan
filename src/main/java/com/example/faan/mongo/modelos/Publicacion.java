package com.example.faan.mongo.modelos;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
@Document(collection = "Publicaciones")
public class Publicacion {

    @Id
    private BigInteger id;
    private String nombre;
    private String raza;
    private String sexo;
    private TipoAnimal tipoAnimal;
    private TipoPublicacion tipoPublicacion;
    private String descripcionEspecifica;
    private Date fecha; // Mantenemos la fecha como un objeto Date
    private String ubicacion;
    private Boolean estadoRescatado;
    private byte[] foto;

    // Constructor Fecha



    public Publicacion(BigInteger id, String nombre, String raza, String sexo, TipoAnimal tipoAnimal, TipoPublicacion tipoPublicacion, String descripcionEspecifica, Date fecha, String ubicacion, Boolean estadoRescatado, byte[] foto) {
        this.id = id;
        this.nombre = nombre;
        this.raza = raza;
        this.sexo = sexo;
        this.tipoAnimal = tipoAnimal;
        this.tipoPublicacion = tipoPublicacion;
        this.descripcionEspecifica = descripcionEspecifica;
        this.fecha = fecha;
        this.ubicacion = ubicacion;
        this.estadoRescatado = estadoRescatado;
        this.foto = foto;
    }

    // Método para obtener la fecha formateada como "yyyy-MM-dd"
    public String getFechaSinHora() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(fecha);
    }

    // Setter para la fecha
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
