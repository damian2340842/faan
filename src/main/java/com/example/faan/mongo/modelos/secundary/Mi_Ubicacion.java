package com.example.faan.mongo.modelos.secundary;

import com.example.faan.mongo.modelos.Usuario;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Mi_Ubicacion")
public class Mi_Ubicacion {
    @Id
    private String id;
    private Usuario userId;
    private double latitude;
    private double longitude;

    public Mi_Ubicacion() {
    }

    public Mi_Ubicacion(Usuario userId, double latitude, double longitude) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getUserId() {
        return userId;
    }

    public void setUserId(Usuario userId) {
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}