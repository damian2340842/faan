package com.example.faan.mongo.emailRecover.Services;


import com.example.faan.mongo.emailRecover.Dtos.EmailValuesDTO;

public interface EmailService {
    boolean sendEmail(EmailValuesDTO values);

}
