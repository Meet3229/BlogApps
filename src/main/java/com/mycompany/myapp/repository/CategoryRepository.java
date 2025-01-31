package com.mycompany.myapp.repository;



import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.Category;
import java.util.Optional;



/**
 * Spring Data MongoDB repository for the Post entity.
 */
@Repository
public interface CategoryRepository extends MongoRepository<Category, Object> {

    
    Optional<Category> findById(ObjectId id);
}
