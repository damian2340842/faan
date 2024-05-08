package com.example.faan.mongo.file.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ApiError implements Serializable {

    private String backedMessage;

    private String message;

    private int httpCode;

    private LocalDateTime time;
}
