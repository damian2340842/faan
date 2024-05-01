package com.example.faan.mongo.modelos;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.time.LocalDate; // Importa la clase LocalDate
import java.time.ZoneId;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    // Nuevo atributo para mostrar la fecha sin hora
    private String fechaSinHora;

    // Constructor

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

        // Formateamos la fecha sin la hora y la guardamos en el atributo correspondiente
        LocalDate localDate = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.fechaSinHora = localDate.toString();
    }

    // Getters y setters para el nuevo atributo
    public String getFechaSinHora() {
        return fechaSinHora;
    }

    public void setFechaSinHora(String fechaSinHora) {
        this.fechaSinHora = fechaSinHora;
    }
}
