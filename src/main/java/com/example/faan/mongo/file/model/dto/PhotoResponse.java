package com.example.faan.mongo.file.model.dto;

import com.example.faan.mongo.file.model.entity.Photo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PhotoResponse implements Serializable {

    private final String status;

    private Photo message;
}
