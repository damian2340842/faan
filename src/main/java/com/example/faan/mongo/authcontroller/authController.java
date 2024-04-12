package com.example.faan.mongo.authcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class authController {
   @PostMapping(value = "login")
    public String login(){
       return "loging from public endpoint";
   }
    @PostMapping(value = "register")
    public String register(){
        return "register from public endpoint";
    }

}
