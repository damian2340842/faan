package com.example.faan.mongo.emailRecover.Dtos;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailValuesDTO {

    private String mailFrom;
    private String mailTo;
    private String subject;
    private String username;
    private String jwt;

}
