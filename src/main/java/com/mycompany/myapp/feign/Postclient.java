package com.mycompany.myapp.feign;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mycompany.myapp.domain.Post;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "restheart-add-post", url = "http://localhost:8080", decode404 = true)
public interface Postclient {

    // Method to save a new post (POST request)
    @PostMapping("/BlogApps/post")
    public ResponseEntity<Void> save(@RequestBody Post post) throws URISyntaxException;

    // Method to update an existing post (PUT request)
    @PutMapping("/BlogApps/post/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody Post post) throws URISyntaxException;

    // Method to partially update an existing post (PATCH request)
    @PatchMapping("/BlogApps/post/{id}")
    public ResponseEntity<Void> patchUpdate(@PathVariable("id") String id, @RequestBody Post post) throws URISyntaxException;

    // Method to get a post by id (GET request)
    @GetMapping("/BlogApps/post/{id}")
    public ResponseEntity<Post> getById(@PathVariable("id") String id);

    // Method to get all posts (GET request)
    @GetMapping("/BlogApps/post")
    public ResponseEntity<List<Post>> findAll();

    // Method to delete a post by id (DELETE request)
    @DeleteMapping("/BlogApps/post/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id);
}
