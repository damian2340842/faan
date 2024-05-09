package com.example.faan.mongo.file.service;

import com.example.faan.mongo.Repository.PublicacionRepository;
import com.example.faan.mongo.Repository.UsuarioRepository;
import com.example.faan.mongo.Service.CounterService;
import com.example.faan.mongo.Service.IPostService;
import com.example.faan.mongo.exception.DuplicatedObjectFoundException;
import com.example.faan.mongo.exception.ObjectNotFoundException;
import com.example.faan.mongo.file.event.PostCreatedEvent;
import com.example.faan.mongo.file.model.dto.PhotoResponse;
import com.example.faan.mongo.file.model.entity.Photo;
import com.example.faan.mongo.file.repository.IPhotoRepository;
import com.example.faan.mongo.file.util.MethodsConverter;
import com.example.faan.mongo.file.util.State;
import com.example.faan.mongo.jwt.JwtService;
import com.example.faan.mongo.modelos.AuthResponse;
import com.example.faan.mongo.modelos.EnumsFijo.Role;
import com.example.faan.mongo.modelos.EnumsFijo.TipoPublicacion;
import com.example.faan.mongo.modelos.Publicacion;
import com.example.faan.mongo.modelos.Usuario;
import com.example.faan.mongo.modelos.dto.SavePost;
import com.example.faan.mongo.modelos.entity.Post;
import lombok.RequiredArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PhotoServiceImpl implements IPhotoService {

    private final IPhotoRepository photoRepository;

    private final PublicacionRepository publicacionRepository;

    private final IPostService postService;

    private final UsuarioRepository usuarioRepository;

    private final CounterService counterService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public PhotoResponse uploadPhoto(MultipartFile file) throws IOException {
        String originalFileName = Objects.requireNonNull(file.getOriginalFilename()).replaceAll("\\\\s+", "");
        String uniqueFileName = UUID.randomUUID() + "_" + LocalDateTime.now() + "_" + MethodsConverter.normalizeFileName(originalFileName);
        byte[] fileBytes = file.getBytes();
        String fileHash = calculateHash(fileBytes);

        Photo existingPhoto = photoRepository.findByImageHash(fileHash);
        if (existingPhoto != null) {
            return new PhotoResponse("Photo already exists", existingPhoto);
        }

        Photo photo = new Photo(uniqueFileName);
        photo.setImage(new Binary(BsonBinarySubType.BINARY, fileBytes));
        photo.setFileType(file.getContentType());
        photo.setImageHash(fileHash);
        photo = photoRepository.insert(photo);

        if (ObjectUtils.isEmpty(photo)) {
            return new PhotoResponse("Photo not saved");
        }

        return new PhotoResponse("Photo saved successfully", photo);
    }

    @Transactional(readOnly = true)
    public Photo getPhoto(String id) {
        return photoRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Photo not found"));
    }

    @Transactional
    @Override
    public PhotoResponse updatePostReference(Long id, MultipartFile file) throws IOException {
        PhotoResponse photoResponse = uploadPhoto(file);

        if(photoResponse.getMessage().equals("Photo not saved") || ObjectUtils.isEmpty(photoResponse)) {
            return new PhotoResponse("Photo not update in post with id:" + id);
        }

        Publicacion post = publicacionRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Post not found"));

        post.setPhoto(photoResponse.getMessage());

        publicacionRepository.save(post);

        return new PhotoResponse("Photo and Post updated successfully");
    }

    @Transactional
    @Override
    public PhotoResponse updateUserReference(String username, MultipartFile file) throws IOException {
        PhotoResponse photoResponse = uploadPhoto(file);

        if(photoResponse.getMessage().equals("Photo not saved") || ObjectUtils.isEmpty(photoResponse)) {
            return new PhotoResponse("Photo not update in user with username:" + username);
        }

        Usuario user = usuarioRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User not found"));

        user.setPhoto(photoResponse.getMessage());

        usuarioRepository.save(user);

        return new PhotoResponse("Photo and User updated successfully");
    }

    @Transactional
    @Override
    public AuthResponse registerUserWithPhoto(Usuario usuario, MultipartFile file) throws IOException {
        PhotoResponse photoResponse = uploadPhoto(file);

        Optional<Usuario> user = usuarioRepository.findByUsername(usuario.getUsername());

        if(user.isPresent()) {
            throw new DuplicatedObjectFoundException("Username: " + usuario.getUsername() + " already exists");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setId(counterService.getNextSequence("usuario_id"));
        usuario.setPhoto(photoResponse.getMessage());
        usuario.setRole(Role.USER);

        usuarioRepository.insert(usuario);

        String token = jwtService.getToken(usuario);

        return AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .build();
    }

    @Transactional
    @Override
    public SavePost registerPostWithPhoto(SavePost savePost, MultipartFile file) throws IOException {
        Post post = convertToPostDocument(savePost);

        if ( post.getTypePost().name().equals(TipoPublicacion.ADOPCION.name()) ) {
            Usuario user = usuarioRepository.findByUsername(savePost.getAuthor().getName()).orElseThrow(() -> new ObjectNotFoundException("User not found"));

            checkUserAllowedToCreatePost(user);
        }

        PhotoResponse photoResponse = uploadPhoto(file);

        post.setPhoto(photoResponse.getMessage());

        postService.savePost(post);

        applicationEventPublisher.publishEvent(new PostCreatedEvent(savePost));

        return savePost;
    }

    private String calculateHash(byte[] fileBytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(fileBytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error calculating hash", e);
        }
    }

    private Post convertToPostDocument(SavePost post) {
        Post postDocument = new Post();
        Usuario user = usuarioRepository.findByUsername(post.getAuthor().getName()).orElseThrow(() -> new ObjectNotFoundException("User not found"));

        postDocument.setTitle(post.getTitle());
        postDocument.setAuthorComment(post.getAuthorComment());
        postDocument.setAuthor(!ObjectUtils.isEmpty(user) ? user : null);
        postDocument.setTypePost(TipoPublicacion.valueOf(post.getTypePost()));
        postDocument.setAnimal(post.getAnimal());
        postDocument.setLocation(post.getLocation());
        postDocument.setState(State.valueOf(post.getState()));

        return postDocument;
    }

    private void checkUserAllowedToCreatePost(Usuario user) {
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("User not allowed to create adoption post");
        }
    }
}
