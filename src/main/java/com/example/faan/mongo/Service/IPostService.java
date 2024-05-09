package com.example.faan.mongo.Service;

import com.example.faan.mongo.file.util.State;
import com.example.faan.mongo.modelos.EnumsFijo.TipoPublicacion;
import com.example.faan.mongo.modelos.dto.LikedPost;
import com.example.faan.mongo.modelos.dto.SavePost;
import com.example.faan.mongo.modelos.entity.Post;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.time.LocalDateTime;

public interface IPostService {

    public Post savePost(Post post);

    public Page<SavePost> getPosts(int pageNumber, int pageSize);

    public Post getPostById(String id);

    public void deletePost(String id);

    public void updatePost(SavePost post);

    public void updateStatePost(String id, State state);

    public String likePost(LikedPost likedPost);

    public Page<SavePost> getLikedPostsByAuthorId(String id, int pageNumber, int pageSize);

    public Page<SavePost> getPostsByType(TipoPublicacion type, int pageNumber, int pageSize);

    public Page<SavePost> getPostsByState(State state, int pageNumber, int pageSize);

    public Page<SavePost> getPostsByTypeAndState(TipoPublicacion type, State state, int pageNumber, int pageSize);

    public Page<SavePost> getPostsByUser(String user, int pageNumber, int pageSize);

    public Page<SavePost> getPostsByUserId(String userId, int pageNumber, int pageSize);

    public Page<SavePost> getPostsByTypeAndUser(TipoPublicacion type, String user, int pageNumber, int pageSize);

    public Page<SavePost> getPostsByStateAndUser(State state, String user, int pageNumber, int pageSize);

    public Page<SavePost> getPostsByTypeAndStateAndUser(TipoPublicacion type, State state, String user, int pageNumber, int pageSize);

    public Page<SavePost> getPostsByTypeAndStateAndDate(TipoPublicacion type, State state, LocalDateTime date, int pageNumber, int pageSize);

    public SavePost getSavePostById(String id);

    public Page<SavePost> getAllPostsToSavePost(int pageNumber, int pageSize);
}