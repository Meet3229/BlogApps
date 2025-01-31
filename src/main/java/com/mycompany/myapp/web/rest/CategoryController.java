package com.mycompany.myapp.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.myapp.domain.Category;
import com.mycompany.myapp.feign.CategoryClient;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryClient categoryClient;


    
    public CategoryController(CategoryClient categoryClient) {
        this.categoryClient = categoryClient;
    }

    // Create a new category
    @PostMapping("/blogApps/category")
    public ResponseEntity<?> createCategory(@RequestBody Category category) throws URISyntaxException {
        log.debug("REST request to save Category : {}", category);

        // Ensure the ID is null before saving
        if (category.getId() != null) {
            throw new BadRequestAlertException("A new category cannot already have an ID", "Category", "idexists");
        }

        // Call Feign client to save the category
        ResponseEntity<Void> saveCategory = categoryClient.save(category);
        return saveCategory;
    }

    // Update an existing category
    @PutMapping("/blogApps/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") String id, @RequestBody Category category)
            throws URISyntaxException {
        log.debug("REST request to update Category : {}", category);

        ResponseEntity<Void> updateCategory = categoryClient.update(id, category);
        return updateCategory;
    }

    // Get a category by ID
    @GetMapping("/blogApps/category/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable("id") String id) {
        log.debug("REST request to get Category : {}", id);

        ResponseEntity<Category> category = categoryClient.getById(id);
        return category;
    }

    // Get all categories
    @GetMapping("/blogApps/category")
    public ResponseEntity<List<Category>> getAllCategories() {
        log.debug("REST request to get all Categories");
        ResponseEntity<List<Category>> categories = categoryClient.findAll();
        return categories;
    }

    // Delete a category by ID
    @DeleteMapping("/blogApps/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") String id) {
        log.debug("REST request to delete Category : {}", id);

        ResponseEntity<Void> deleteCategory = categoryClient.delete(id);
        return deleteCategory;
    }


}
