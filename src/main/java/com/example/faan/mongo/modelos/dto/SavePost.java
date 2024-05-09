package com.example.faan.mongo.modelos.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.io.Serializable;

@Data
public class SavePost implements Serializable {

    private String id;

    private String title;

    private String authorComment;

    private String typePost;

    private Author author;

    private Animal animal;

    private GeoJsonPoint location;

    private String state;
}