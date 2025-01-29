package com.mycompany.myapp.feign;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mycompany.myapp.domain.Comment;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "restheart-add-comment", url = "http://localhost:8080", decode404 = true)
public interface CommentClient {

    // Method to save a new comment (POST request)
    @PostMapping("/blogApps/comment")
    public ResponseEntity<Void> save(@RequestBody Comment comment) throws URISyntaxException;

    // Method to update an existing comment (PUT request)
    @PutMapping("/blogApps/comment/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody Comment comment) throws URISyntaxException;

    // Method to get a comment by id (GET request)
    @GetMapping("/blogApps/comment/{id}")
    public ResponseEntity<Comment> getById(@PathVariable("id") String id);

    // Method to get all comments (GET request)
    @GetMapping("/blogApps/comment")
    public ResponseEntity<List<Comment>> findAll();

    // Method to delete a comment by id (DELETE request)
    @DeleteMapping("/blogApps/comment/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id);
}
