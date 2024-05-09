package com.example.faan.mongo.modelos.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LikedPost implements Serializable {

    private String postId;

    private Author author;
}
