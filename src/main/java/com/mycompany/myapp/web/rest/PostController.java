package com.mycompany.myapp.web.rest;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.myapp.domain.Category;
import com.mycompany.myapp.domain.CreateInfo;
import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.domain.RefType;
import com.mycompany.myapp.domain.UpdateInfo;
import com.mycompany.myapp.domain.RefType.RefTo;
import com.mycompany.myapp.feign.CategoryClient;
import com.mycompany.myapp.feign.Postclient;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping("/api")
public class PostController {

    private final Logger log = LoggerFactory.getLogger(PostController.class);

    private final Postclient postclient;

    private final UserRepository userRepository;

    private final CategoryClient categoryClient;

    public PostController(Postclient postclient, UserRepository userRepository, CategoryClient categoryClient) {
        this.userRepository = userRepository;
        this.postclient = postclient;
        this.categoryClient = categoryClient;
    }

    // Create a new post
    // this code only post create conreoller
    @PostMapping("/blogApps/post")
    public ResponseEntity<?> createPost(@RequestBody Post post) throws URISyntaxException {
        log.debug("REST request to save Post : {}", post);

        // Ensure the ID is null before saving
        if (post.getId() != null) {
            throw new BadRequestAlertException("A new post cannot already have an ID", "Post", "idexists");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        post.setCreateInfo(CreateInfo.builder()
                .user(new RefType(userRepository.findOneByLogin(currentPrincipalName).get().getId(), RefTo.User))
                .createdDate(Instant.now())
                .build());

        post.setUpdateInfo(UpdateInfo.builder()
                .user(new RefType(userRepository.findOneByLogin(currentPrincipalName).get().getId(), RefTo.User))
                .lastModifiedDate(Instant.now())
                .build());

        // Call Feign client to save the post
        return postclient.save(post);
    }

    // Update an existing post
    //Hello Harsh 
    @PutMapping("/blogApps/post/{id}")
    public ResponseEntity<?> updatePost(@PathVariable("id") String id, @RequestBody Post post)
            throws URISyntaxException {
        log.debug("REST request to update Post : {}", post);

        return postclient.update(id, post);
        
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


    @PostMapping("/blogApps/category/{categoryId}/post")
    public ResponseEntity<?> addPostToCategory(@PathVariable("categoryId") String categoryId, @RequestBody Post post)
            throws URISyntaxException {
        log.debug("REST request to add Post : {} to Category : {}", post, categoryId);
        post.setId(new ObjectId().toHexString());
        post.setCategory(new RefType(new ObjectId(categoryId).toHexString(), RefTo.Category));

        Category cat = categoryClient.getById(categoryId).getBody();
        cat.getPosts().add(new RefType(new ObjectId(post.getId()).toHexString(), RefTo.Post));
        categoryClient.update(categoryId, cat);


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        post.setCreateInfo(CreateInfo.builder()
                .user(new RefType(userRepository.findOneByLogin(currentPrincipalName).get().getId(), RefTo.User))
                .createdDate(Instant.now())
                .build());

        post.setUpdateInfo(UpdateInfo.builder()
                .user(new RefType(userRepository.findOneByLogin(currentPrincipalName).get().getId(), RefTo.User))
                .lastModifiedDate(Instant.now())
                .build());

        return postclient.save(post);
    }

}
