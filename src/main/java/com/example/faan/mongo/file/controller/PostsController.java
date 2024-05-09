package com.example.faan.mongo.file.controller;

import com.example.faan.mongo.Service.IPostService;
import com.example.faan.mongo.file.util.State;
import com.example.faan.mongo.modelos.EnumsFijo.TipoPublicacion;
import com.example.faan.mongo.modelos.dto.LikedPost;
import com.example.faan.mongo.modelos.dto.SavePost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
@RestController
@RequiredArgsConstructor
public class PostsController {

    private final IPostService postService;

    /* Use permitAll because all users can use it.
     * If we use Anonymous, only not authenticated users will be able to use it.
     */
    @PreAuthorize("permitAll()")
    @GetMapping("/type")
    public ResponseEntity<Page<SavePost>> findByTypePost(@RequestParam("postType") TipoPublicacion postType,
                                                     @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Page<SavePost> posts = postService.getPostsByType(postType, pageNumber, pageSize);
        return ResponseEntity.ok().body(posts);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/state")
    public ResponseEntity<Page<SavePost>> findByState(@RequestParam("state") State state,
                                                  @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Page<SavePost> posts = postService.getPostsByState(state, pageNumber, pageSize);
        return ResponseEntity.ok().body(posts);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/type-state")
    public ResponseEntity<Page<SavePost>> findByTypeAndState(@RequestParam("postType") TipoPublicacion postType, @RequestParam("state") State state,
                                                         @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Page<SavePost> posts = postService.getPostsByTypeAndState(postType, state, pageNumber, pageSize);
        return ResponseEntity.ok().body(posts);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/author-id")
    public ResponseEntity<Page<SavePost>> findByAuthorId(@RequestParam String authorId,
                                                         @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok().body(postService.getPostsByUserId(authorId, pageNumber, pageSize));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/author")
    public ResponseEntity<Page<SavePost>> findByAuthorUsername(@RequestParam String authorId,
                                                           @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Page<SavePost> posts = postService.getPostsByUserId(authorId, pageNumber, pageSize);

        return ResponseEntity.ok().body(posts);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/type-author-id")
    public ResponseEntity<Page<SavePost>> findByTypePostAndAuthorUsername(@RequestParam("postType") TipoPublicacion postType, @RequestParam("author") String author,
                                                                      @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Page<SavePost> posts = postService.getPostsByTypeAndUser(postType, author, pageNumber, pageSize);
        return ResponseEntity.ok().body(posts);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/state-author-id")
    public ResponseEntity<Page<SavePost>> findByStateAndAuthorId(@RequestParam("state") State state,
                                                                   @RequestParam("author") String author,
                                                                   @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Page<SavePost> posts = postService.getPostsByStateAndUser(state, author, pageNumber, pageSize);
        return ResponseEntity.ok().body(posts);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/type-state-author-id")
    public ResponseEntity<Page<SavePost>> findByTypePostAndStateAndAuthorId(@RequestParam("postType") TipoPublicacion postType,
                                                                              @RequestParam("state") State state, @RequestParam("author") String author,
                                                                              @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Page<SavePost> posts = postService.getPostsByTypeAndStateAndUser(postType, state, author, pageNumber, pageSize);
        return ResponseEntity.ok().body(posts);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/get-dto/{id}")
    public ResponseEntity<SavePost> getPostById(@PathVariable String id) {
        SavePost post = postService.getSavePostById(id);
        return ResponseEntity.ok().body(post);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/like")
    public ResponseEntity<String> likePost(@RequestBody LikedPost likedPost) {
        String status = postService.likePost(likedPost);
        return ResponseEntity.ok(status);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/all-dto")
    public ResponseEntity<Page<SavePost>> getAllPostsToSavePost(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        Page<SavePost> posts = postService.getAllPostsToSavePost(pageNumber, pageSize);
        return ResponseEntity.ok().body(posts);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/get-liked-posts")
    public ResponseEntity<Page<SavePost>> getLikedPostsByAuthorId(@RequestParam("authorId") String authorId,
                                                                  @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok().body(postService.getLikedPostsByAuthorId(authorId, pageNumber, pageSize));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<String> updatePost(@RequestBody SavePost post) {
        postService.updatePost(post);
        return ResponseEntity.ok("Post updated successfully");
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/update-state/{id}")
    public ResponseEntity<String> updateStatePost(@PathVariable String id, @RequestParam State state) {
        postService.updateStatePost(id, state);
        return ResponseEntity.ok("Post state updated successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable String id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully");
    }
}
