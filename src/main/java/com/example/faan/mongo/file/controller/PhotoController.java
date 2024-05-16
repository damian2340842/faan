package com.example.faan.mongo.file.controller;

import com.example.faan.mongo.file.model.entity.Photo;
import com.example.faan.mongo.file.service.IPhotoService;
import com.example.faan.mongo.modelos.Usuario;
import com.example.faan.mongo.modelos.dto.SavePost;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class PhotoController {

    private final IPhotoService photoService;

    public PhotoController(IPhotoService photoService) {
        this.photoService = photoService;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("photo") MultipartFile photo) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(photoService.uploadPhoto(photo));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(value = "/get-photo/{id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String id) {
        Photo photo = photoService.getPhoto(id);
        byte[] imageData = photo.getImage().getData();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(photo.getFileType()));

        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/update-post/{id}")
    public ResponseEntity<?> updatePhotoInPost(@PathVariable Long id, @RequestParam("photo") MultipartFile photo) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(photoService.updatePostReference(id, photo));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/update-simplified-post/{id}")
    public ResponseEntity<?> updateSimplifiedPhotoInPost(@PathVariable String id, @RequestParam("photo") MultipartFile photo) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(photoService.updatePostReference(id, photo));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/update-user/{username}")
    public ResponseEntity<?> updatePhotoInUser(@PathVariable String username, @RequestParam("photo") MultipartFile photo) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(photoService.updateUserReference(username, photo));
    }
//Registro Principal :)OK
    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/register-user", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<?> registerUserWithPicture(@RequestPart("usuario") Usuario usuario, @RequestPart("photo") MultipartFile photo) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(photoService.registerUserWithPhoto(usuario, photo));
    }

    @PreAuthorize("permitAll()")
    @PostMapping(value = "/register-post", consumes = {"multipart/form-data"})
    public ResponseEntity<?> registerPostWithPicture(@RequestPart("savePost") SavePost savePost, @RequestPart("photo") MultipartFile photo) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(photoService.registerPostWithPhoto(savePost, photo));
    }
}
