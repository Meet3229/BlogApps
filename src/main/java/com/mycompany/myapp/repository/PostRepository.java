package com.mycompany.myapp.repository;



import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.Post;

import java.util.List;
import java.util.Optional;



/**
 * Spring Data MongoDB repository for the Post entity.
 */
@Repository
public interface PostRepository extends MongoRepository<Post, Object> {

    
    Optional<Post> findById(ObjectId id);


        
}
