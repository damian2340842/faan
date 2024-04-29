package com.example.faan.mongo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition
@SpringBootApplication
public class FaanApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaanApplication.class, args);
    }

}
