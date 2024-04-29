package com.example.faan.mongo.emailRecover.Services;


import com.example.faan.mongo.emailRecover.Dtos.EmailValuesDTO;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;


@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ITemplateEngine ITemplateEngine;

    @Value("${mail.urlFront}")
    private String urlFront;

    @Override
    public boolean sendEmail(EmailValuesDTO values) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("username", values.getUsername());
            model.put("url", urlFront + values.getJwt());
            context.setVariables(model);
            String htmlText = ITemplateEngine.process("email_template",context);
            helper.setFrom(values.getMailFrom());
            helper.setTo(values.getMailTo());
            helper.setSubject(values.getSubject());
            helper.setText(htmlText,true);
            javaMailSender.send(message);
            return true;

        } catch (Exception e) {
            return false;
        }
    }


    public void enviarCorreo(String nombre, String correo, String asunto, String mensaje) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        String destinatario = "faanproteccionanimal@gmail.com"; // Destinatario fijo
        mailMessage.setTo(destinatario);
        mailMessage.setSubject(asunto);
        mailMessage.setText("Nombre: " + nombre + "\nCorreo: " + correo + "\nMensaje: " + mensaje);

        javaMailSender.send(mailMessage);
    }

}
