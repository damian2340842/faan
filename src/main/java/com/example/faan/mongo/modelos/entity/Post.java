package com.example.faan.mongo.modelos.entity;

import com.example.faan.mongo.file.model.entity.Photo;
import com.example.faan.mongo.file.util.State;
import com.example.faan.mongo.modelos.EnumsFijo.TipoPublicacion;
import com.example.faan.mongo.modelos.Usuario;
import com.example.faan.mongo.modelos.dto.Animal;
import com.example.faan.mongo.modelos.dto.Author;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "posts")
public class Post {

    @Id
    private String id;

    private String title;

    private String authorComment;

    private LocalDateTime createAt = LocalDateTime.now();

    private List<BigInteger> likes;

    private Animal animal;

    private GeoJsonPoint location;

    @Field
    @JsonSerialize(using = ToStringSerializer.class)
    private State state;

    @Field
    @JsonSerialize(using = ToStringSerializer.class)
    private TipoPublicacion typePost;

    @DBRef
    private Photo photo;

    @DBRef
    private Usuario author;
}
