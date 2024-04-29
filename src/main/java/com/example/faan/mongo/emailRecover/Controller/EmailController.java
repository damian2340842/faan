package com.example.faan.mongo.emailRecover.Controller;


import com.example.faan.mongo.Service.UsuarioService;
import com.example.faan.mongo.emailRecover.Dtos.CambiarPasswordDTO;
import com.example.faan.mongo.emailRecover.Dtos.CorreoRequest;
import com.example.faan.mongo.emailRecover.Dtos.EmailValuesDTO;
import com.example.faan.mongo.emailRecover.MessageCode.Message;
import com.example.faan.mongo.emailRecover.Services.EmailServiceImpl;
import com.example.faan.mongo.modelos.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@CrossOrigin(origins = {"http://localhost:4200", "http://10.0.2.2:8080"})
@RestController
@RequestMapping("/auth")
public class EmailController {
    @Value("${spring.mail.username}")
    private String sendFrom;
    private EmailServiceImpl emailService;
    private UsuarioService usuarioService;
    private PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    public EmailController(EmailServiceImpl emailService, UsuarioService usuarioService, PasswordEncoder passwordEncoder){
        this.emailService = emailService;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/email/sendRecuperacionPassword/{email}")
    public ResponseEntity<?> sendEmailRecuperacion(@PathVariable("email") String email){

        try {
            Usuario usuario = usuarioService.findByPersonaEmail(email);
            if (usuario == null) {
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(Message.ERROR_404);
            }
            EmailValuesDTO values = new EmailValuesDTO();
            values.setMailFrom(sendFrom);
            values.setMailTo(usuario.getEmail());
            values.setUsername(usuario.getNombre()  + ' ' + usuario.getApellido());
            values.setSubject("RESTAURAR CONSTRASEÑA");

            String tokenPassword = generateRandomNumericToken(5);

            values.setJwt(tokenPassword);
            usuario.setTokenPassword(tokenPassword);

            if(emailService.sendEmail(values) == true) {
                usuarioService.saveUsuario(usuario);
                String responseBody = objectMapper.writeValueAsString(Message.SUCCESSFUL_200);
                return ResponseEntity.status(HttpStatus.OK).body(responseBody);
            }else {
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Message.ERROR_400);
            }

        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Message.ERROR_500);
        }

    }

    @PostMapping("/cambiarContraseniaUsuario")
    public ResponseEntity<?> cambiarContraseniaUsuario(@Valid @RequestBody CambiarPasswordDTO dto, BindingResult bindingResult){
        try {
            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>("Campos mal puestos", HttpStatus.BAD_REQUEST);
            }
            if (!dto.getPassword().equals(dto.getPasswordr())) {
                return new ResponseEntity<>("No coinciden", HttpStatus.BAD_REQUEST);
            }
            Usuario usuario = usuarioService.findByTokenPassword(dto.getToken());
            if (usuario == null) {
                return new ResponseEntity<>("No encontrado el usuario", HttpStatus.NOT_FOUND);
            }

            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            usuario.setTokenPassword(null);
            usuarioService.saveUsuario(usuario);
            return new ResponseEntity<>(objectMapper.writeValueAsString("SUCCESSFUL"), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @PostMapping("/enviar")
    public ResponseEntity<Map<String, String>> enviarCorreo(@RequestBody CorreoRequest correoRequest) {
        try {
            emailService.enviarCorreo(correoRequest.getNombre(), correoRequest.getCorreo(), correoRequest.getAsunto(), correoRequest.getMensaje());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Correo enviado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al enviar el correo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    private String generateRandomNumericToken(int length) {
        StringBuilder token = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            token.append(random.nextInt(10)); // Añadir un dígito aleatorio
        }
        return token.toString();
    }

}
