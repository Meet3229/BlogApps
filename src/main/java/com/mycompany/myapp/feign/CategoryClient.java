package com.mycompany.myapp.feign;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mycompany.myapp.domain.Category;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(name = "restheart-category", url = "http://localhost:8080", decode404 = true)
public interface CategoryClient {

    // Method to save a new category (POST request)
    @PostMapping("/BlogApps/category")
    public ResponseEntity<Void> save(@RequestBody Category category) throws URISyntaxException;

    // Method to update an existing category (PUT request)
    @PutMapping("/BlogApps/category/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody Category category) throws URISyntaxException;

    // Method to get a category by id (GET request)
    @GetMapping("/BlogApps/category/{id}")
    public ResponseEntity<Category> getById(@PathVariable("id") String id);

    // Method to get all categories (GET request)
    @GetMapping("/BlogApps/category")
    public ResponseEntity<List<Category>> findAll();

    // Method to delete a category by id (DELETE request)
    @DeleteMapping("/BlogApps/category/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id);

}
