package com.example.faan.mongo.modelos.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class Author implements Serializable {

    private BigInteger id;

    private String name;

    private String email;

    private String phone;

    private byte[] data;
}
