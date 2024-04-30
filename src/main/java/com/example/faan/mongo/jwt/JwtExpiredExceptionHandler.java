package com.example.faan.mongo.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class JwtExpiredExceptionHandler {
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleJwtExpiredException(ExpiredJwtException ex) {
        String errorMessage = "El token JWT ha expirado " + ex.getMessage() ;
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }
}
