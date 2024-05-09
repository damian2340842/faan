package com.example.faan.mongo.Service;

import com.example.faan.mongo.Repository.IPostRepository;
import com.example.faan.mongo.Repository.UsuarioRepository;
import com.example.faan.mongo.exception.DuplicatedObjectFoundException;
import com.example.faan.mongo.exception.ObjectNotFoundException;
import com.example.faan.mongo.file.util.State;
import com.example.faan.mongo.modelos.EnumsFijo.Role;
import com.example.faan.mongo.modelos.EnumsFijo.TipoPublicacion;
import com.example.faan.mongo.modelos.Usuario;
import com.example.faan.mongo.modelos.dto.Author;
import com.example.faan.mongo.modelos.dto.LikedPost;
import com.example.faan.mongo.modelos.dto.SavePost;
import com.example.faan.mongo.modelos.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImp implements IPostService {

    private final IPostRepository postRepository;

    private final UsuarioRepository usuarioRepository;

    @Override
    public Post savePost(Post post) {

        if (existsDuplicatePost(post)) {
            throw new DuplicatedObjectFoundException("Ya existe una publicación con el mismo título en los últimos 60 segundos");
        }

        return postRepository.save(post);
    }

    @Override
    public Page<SavePost> getPosts(int pageNumber, int pageSize) {
        return postRepository.findAll(PageRequest.of(pageNumber, pageSize))
                .map(this::getSavePostFromPost);
    }

    @Override
    public Post getPostById(String id) {
        return postRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("No se encontró la publicación con el id: " + id));
    }

    @Override //DeleteMyPost
    public void deletePost(String id) {
        postRepository.deleteById(id);
    }

    @Override
    public void updatePost(SavePost post) {
        Post postToUpdate = getPostById(post.getId());

        if (!post.getAuthor().getName().equals(postToUpdate.getAuthor().getUsername()) && !postToUpdate.getAuthor().getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("This post isn't your owner, you don't have permissions.");
        }

        postToUpdate.setTitle(post.getTitle().isEmpty() ? postToUpdate.getTitle() : post.getTitle());

        //This line isn't recommended why the Author post don't have change.
        postToUpdate.setAuthorComment(post.getAuthorComment().isEmpty() ? postToUpdate.getAuthorComment() : post.getAuthorComment());

        postToUpdate.setTypePost(post.getTypePost().isEmpty() ? postToUpdate.getTypePost() : TipoPublicacion.valueOf(post.getTypePost()));
        postToUpdate.setAnimal(ObjectUtils.isEmpty(post.getAnimal()) ? postToUpdate.getAnimal() : post.getAnimal());
        postToUpdate.setLocation(ObjectUtils.isEmpty(post.getLocation()) ? postToUpdate.getLocation() : post.getLocation());
        postToUpdate.setState(post.getState().isEmpty() ? postToUpdate.getState() : State.valueOf(post.getState()));

        postRepository.save(postToUpdate);
    }

    @Override
    public void updateStatePost(String id, State state) {
        Post post = getPostById(id);
        post.setState(state);
        postRepository.save(post);
    }

    @Override
    public String likePost(LikedPost postToLike) {
        Post post = getPostById(postToLike.getPostId());
        String status = "";

        Usuario user = usuarioRepository.findByUsername(postToLike.getAuthor().getName())
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        if (post.getLikes() == null || post.getLikes().isEmpty()) {
            post.setLikes(new ArrayList<>());
            post.getLikes().add(user.getId());
            status = "Post liked successfully";
        } else {
            boolean isLiked = post.getLikes().stream()
                    .anyMatch(like -> like.equals(user.getId()));
            if (isLiked) {
                post.getLikes().remove(user.getId());
                status = "Post disliked successfully";
            } else {
                post.getLikes().add(user.getId());
                status = "Post liked successfully";
            }
        }

        postRepository.save(post);

        return status;
    }

    @Override
    public Page<SavePost> getLikedPostsByAuthorId(String id, int pageNumber, int pageSize) {

        return postRepository.findPostsLikedByAuthor(id, PageRequest.of(pageNumber, pageSize)).map(this::getSavePostFromPost);
    }

    @Override
    public Page<SavePost> getPostsByType(TipoPublicacion type, int pageNumber, int pageSize) {
        return postRepository.findByTypePost(type, PageRequest.of(pageNumber, pageSize))
                .map(this::getSavePostFromPost);
    }

    @Override
    public Page<SavePost> getPostsByState(State state, int pageNumber, int pageSize) {
        return postRepository.findByState(state, PageRequest.of(pageNumber, pageSize))
                .map(this::getSavePostFromPost);
    }

    @Override
    public Page<SavePost> getPostsByTypeAndState(TipoPublicacion type, State state, int pageNumber, int pageSize) {
        return postRepository.findByTypePostAndState(type, state, PageRequest.of(pageNumber, pageSize))
                .map(this::getSavePostFromPost);
    }

    @Override
    public Page<SavePost> getPostsByUser(String author, int pageNumber, int pageSize) {
        return postRepository.findByAuthorUsername(author, PageRequest.of(pageNumber, pageSize))
                .map(this::getSavePostFromPost);
    }

    @Override
    public Page<SavePost> getPostsByUserId(String userId, int pageNumber, int pageSize) {
        Page<Post> post = postRepository.findByAuthorId(userId, PageRequest.of(pageNumber, pageSize));

        System.out.println(post.getContent().stream().map(Post::getAuthor).collect(Collectors.toList()));

        return post.map(this::getSavePostFromPost);
    }

    @Override
    public Page<SavePost> getPostsByTypeAndUser(TipoPublicacion type, String authorId, int pageNumber, int pageSize) {
        return postRepository.findByTypePostAndAuthorId(type, authorId, PageRequest.of(pageNumber, pageSize))
                .map(this::getSavePostFromPost);
    }

    @Override
    public Page<SavePost> getPostsByStateAndUser(State state, String authorId, int pageNumber, int pageSize) {
        return postRepository.findByStateAndAuthorId(state, authorId, PageRequest.of(pageNumber, pageSize))
                .map(this::getSavePostFromPost);
    }

    @Override
    public Page<SavePost> getPostsByTypeAndStateAndUser(TipoPublicacion type, State state, String authorId, int pageNumber, int pageSize) {
        return postRepository.findByTypePostAndStateAndAuthorId(type, state, authorId, PageRequest.of(pageNumber, pageSize))
                .map(this::getSavePostFromPost);
    }

    //This method is not implemented
    @Override
    public Page<SavePost> getPostsByTypeAndStateAndDate(TipoPublicacion type, State state, LocalDateTime date, int pageNumber, int pageSize) {
        return postRepository.findByTypePostAndStateAndCreateAt(type, state, date, PageRequest.of(pageNumber, pageSize))
                .map(this::getSavePostFromPost);
    }

    @Override
    public SavePost getSavePostById(String id) {
        Post post = getPostById(id);

        return getSavePostFromPost(post);
    }

    @Override
    public Page<SavePost> getAllPostsToSavePost(int pageNumber, int pageSize) {

        return getPosts(pageNumber, pageSize);
    }

    private boolean existsDuplicatePost(Post newPost) {
        String tituloNuevaPublicacion = newPost.getTitle();
        LocalDateTime fechaCreacionNuevaPublicacion = newPost.getCreateAt();

        long periodoDuplicadoEnSegundos = 60;

        LocalDateTime fechaLimiteInferior = fechaCreacionNuevaPublicacion.minusSeconds(periodoDuplicadoEnSegundos);

        List<Post> publicacionesDuplicadas = postRepository.findByTitleAndCreateAtBetween(tituloNuevaPublicacion, fechaLimiteInferior, fechaCreacionNuevaPublicacion);

        return !publicacionesDuplicadas.isEmpty();
    }

    private SavePost getSavePostFromPost(Post post) {
        SavePost savePost = new SavePost();

        savePost.setId(post.getId());
        savePost.setTitle(post.getTitle());
        savePost.setAuthorComment(post.getAuthorComment());
        savePost.setAnimal(post.getAnimal());
        savePost.setLocation(post.getLocation());
        savePost.setState(post.getState().toString());
        savePost.setTypePost(post.getTypePost().toString());
        savePost.setAuthor(getAuthorFromPost(post));

        return savePost;
    }

    private Author getAuthorFromPost(Post post) {
        Author author = new Author();

        author.setId(post.getAuthor().getId());
        author.setName(post.getAuthor().getUsername());
        author.setEmail(post.getAuthor().getEmail());
        author.setPhone(post.getAuthor().getTelefono());
        author.setData(post.getAuthor().getPhoto().getImage().getData());

        return author;
    }
}
