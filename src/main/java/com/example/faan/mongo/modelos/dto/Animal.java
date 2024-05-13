package com.example.faan.mongo.modelos.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Animal {

    private String name;

    private String type;

    private String race;

    private String gender;

    private LocalDate date;
}
