package com.example.faan.mongo.Repository;

import com.example.faan.mongo.file.util.State;
import com.example.faan.mongo.modelos.EnumsFijo.TipoPublicacion;
import com.example.faan.mongo.modelos.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface IPostRepository extends MongoRepository<Post, String> {

    Page<Post> findAll(Pageable pageable);

    List<Post> findByTitleAndCreateAtBetween(String title, LocalDateTime createAt, LocalDateTime createAtUp);

    Page<Post> findByTypePost(TipoPublicacion typePost, Pageable pageable);

    Page<Post> findByState(State state, Pageable pageable);

    Page<Post> findByTypePostAndState(TipoPublicacion type, State state, Pageable pageable);

    @Query("{'author.$id': ?0}")
    Page<Post> findByAuthorId(String authorId, Pageable pageable);

    @Query("{'likes': { $in: [?0] }}") //Cuales
    Page<Post> findPostsLikedByAuthor(String authorId, Pageable pageable);

    Page<Post> findByAuthorUsername(String author, Pageable pageable);

    @Query("{'typePost': ?0,'author.$id': ?1}")
    Page<Post> findByTypePostAndAuthorId(TipoPublicacion type, String authorId, Pageable pageable);

    @Query("{'state': ?0,'author.$id': ?1}")
    Page<Post> findByStateAndAuthorId(State state, String authorId, Pageable pageable);

    @Query("{'typePost': ?0, 'state': ?1,'author.$id': ?2}")
    Page<Post> findByTypePostAndStateAndAuthorId(TipoPublicacion type, State state, String authorId, Pageable pageable);

    Page<Post> findByTypePostAndStateAndCreateAt(TipoPublicacion type, State state, LocalDateTime date, Pageable pageable);
}
