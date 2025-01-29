package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.feign.Postclient;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final Logger log = LoggerFactory.getLogger(PostController.class);

    private final Postclient postclient;

    public PostController(Postclient postclient) {
        this.postclient = postclient;
    }

    // Create a new post
    @PostMapping("/blogApps/post")
    public ResponseEntity<?> createPost(@RequestBody Post post) throws URISyntaxException {
        log.debug("REST request to save Post : {}", post);

        // Ensure the ID is null before saving
        if (post.getId() != null) {
            throw new BadRequestAlertException("A new post cannot already have an ID", "Post", "idexists");
        }

        // Call Feign client to save the post
        ResponseEntity<Void> savePost = postclient.save(post);
        return savePost;
    }

    // Update an existing post
    @PutMapping("/blogApps/post/{id}")
    public ResponseEntity<?> updatePost(@PathVariable("id") String id, @RequestBody Post post) throws URISyntaxException {
        log.debug("REST request to update Post : {}", post);    

        ResponseEntity<Void> updatePost = postclient.update(id, post);
        return updatePost;
    }

    // Get a post by ID
    @GetMapping("/blogApps/post/{id}")
    public ResponseEntity<Post> getPost(@PathVariable("id") String id) {
        log.debug("REST request to get Post : {}", id);

        ResponseEntity<Post> post = postclient.getById(id);
        return post;
    }

    // Get all posts
    @GetMapping("/blogApps/post")
    public ResponseEntity<List<Post>> getAllPosts() {
        log.debug("REST request to get all Posts");

        ResponseEntity<List<Post>> posts = postclient.findAll();
        return posts;
    }

    // Delete a post by ID
    @DeleteMapping("/blogApps/post/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") String id) {
        log.debug("REST request to delete Post : {}", id);

        ResponseEntity<Void> deletePost = postclient.delete(id);
        return deletePost;
    }

   
}
