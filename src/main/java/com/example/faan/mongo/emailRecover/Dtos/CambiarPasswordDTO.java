package com.example.faan.mongo.emailRecover.Dtos;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CambiarPasswordDTO {

    @NotBlank
    private String password;
    @NotBlank
    private String passwordr;
    @NotBlank
    private String token;


}
