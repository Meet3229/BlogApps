package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Comment;
import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.domain.RefType;
import com.mycompany.myapp.domain.RefType.RefTo;
import com.mycompany.myapp.feign.CommentClient;
import com.mycompany.myapp.feign.Postclient;
import com.mycompany.myapp.repository.CommentRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentClient commentClient;
    private final Postclient postClient;
    private final CommentRepository commentRepository;

    public CommentController(CommentClient commentClient, Postclient postClient, CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        this.commentClient = commentClient;
        this.postClient = postClient;
    }

    // Create a new comment
    @PostMapping("/blogApps/comment")
    public ResponseEntity<?> createComment(@RequestBody Comment comment) throws URISyntaxException {
        log.debug("REST request to save Comment : {}", comment);

        // Ensure the ID is null before saving
        if (comment.getId() != null) {
            throw new BadRequestAlertException("A new comment cannot already have an ID", "Comment", "idexists");
        }

        ResponseEntity<Void> saveComment = commentClient.save(comment);
        return saveComment;
    }

    // Update an existing comment
    @PutMapping("/blogApps/comment/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") String id, @RequestBody Comment comment)
            throws URISyntaxException {
        log.debug("REST request to update Comment : {}", comment);

        if (!id.equals(comment.getId())) {
            throw new BadRequestAlertException("ID in URL and request body must match", "Comment", "idnotmatch");
        }

        ResponseEntity<Void> updateComment = commentClient.update(id, comment);
        return updateComment;
    }

    // Get a comment by ID
    @GetMapping("/blogApps/comment/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable("id") String id) {
        log.debug("REST request to get Comment : {}", id);

        ResponseEntity<Comment> comment = commentClient.getById(id);
        return comment;
    }

    @GetMapping("/blogApps/comment/post/{id}")

    public ResponseEntity< List<Comment>> getAllCommentByPostId (@PathVariable String id){
        List<Comment> allCommentsByPostId = commentRepository.findAllCommentsByPostId(new ObjectId(id));
        return ResponseEntity.ok().body(allCommentsByPostId);
    }

    // Get all comments for a post
    @GetMapping("/blogApps/comment")
    public ResponseEntity<List<Comment>> getAllComments() {
        log.debug("REST request to get all Comments");

        ResponseEntity<List<Comment>> comments = commentClient.findAll();
        return comments;
    }

    // Delete a comment by ID
    @DeleteMapping("/blogApps/comment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") String id) {
        log.debug("REST request to delete Comment : {}", id);
        ResponseEntity<Void> deleteComment = commentClient.delete(id);
        return deleteComment;
    }
    

    // Add a comment to a specific post
    @PostMapping("/blogApps/post/{postId}/comment")
    public ResponseEntity<?> addCommentToPost(@PathVariable("postId") String postId, @RequestBody Comment comment)
            throws URISyntaxException {
        log.debug("REST request to add Comment : {} to Post : {}", comment, postId);
        comment.setId(new ObjectId().toHexString());
        comment.setPost(new RefType( new ObjectId(postId).toHexString()   ,  RefTo.Post));

        ResponseEntity<Void> addCommentToPost = commentClient.save(comment);
        String location = addCommentToPost.getHeaders().get("Location").get(0);
       String commentid = location.substring(location.lastIndexOf("/") + 1);

        // Set the postId in the comment object before saving (assuming Comment has a
        // setPostId method)

        ResponseEntity<Post> byId = postClient.getById(postId);
        Post post = byId.getBody();
        post.getComments().add(new RefType(new ObjectId(commentid).toHexString(), RefTo.Comment));
        postClient.update(postId, post);
        
        // Call Feign client to add the comment to the specific post
        return addCommentToPost;
    }

   
    @GetMapping("/blogApps/post/{postId}/comment")
    public ResponseEntity<?> getallcommnetbyId(@PathVariable("postId") String postId){
        List<Comment> allCommentsByPostId = commentRepository.findAllCommentsByPostId(new ObjectId(postId));
        return ResponseEntity.ok().body(allCommentsByPostId);
    }



    
}
