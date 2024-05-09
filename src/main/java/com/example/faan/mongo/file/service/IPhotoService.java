package com.example.faan.mongo.file.service;

import com.example.faan.mongo.file.model.dto.PhotoResponse;
import com.example.faan.mongo.file.model.entity.Photo;
import com.example.faan.mongo.modelos.AuthResponse;
import com.example.faan.mongo.modelos.Usuario;
import com.example.faan.mongo.modelos.dto.SavePost;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IPhotoService {

    public PhotoResponse uploadPhoto(MultipartFile file) throws IOException;

    public Photo getPhoto(String id);

    public PhotoResponse updatePostReference(Long id, MultipartFile file) throws IOException;

    //This method is to update the post(Second simplify collection) photo
    public PhotoResponse updatePostReference(String id, MultipartFile file) throws IOException;

    public PhotoResponse updateUserReference(String username, MultipartFile file) throws IOException;

    public AuthResponse registerUserWithPhoto(Usuario usuario, MultipartFile file) throws IOException;

    public SavePost registerPostWithPhoto(SavePost savePost, MultipartFile file) throws IOException;
}
