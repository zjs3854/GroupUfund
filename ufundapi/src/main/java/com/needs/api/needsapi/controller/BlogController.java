package com.needs.api.needsapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.needs.api.needsapi.persistence.BlogDAO;
import com.needs.api.needsapi.model.Posts;

/**
 * Handles the REST API requests for the Need resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author IceCaps
 */

 @RestController
 @RequestMapping("posts")
 public class BlogController {
     private static final Logger LOG = Logger.getLogger(BlogController.class.getName());
     private BlogDAO blogDao;
 
     /**
      * Creates a REST API controller to reponds to requests
      * 
      * @param blogDao The {@link BlogDAO Blog Data Access Object} to perform CRUD operations
      * <br>
      * This dependency is injected by the Spring Framework
      */
     public BlogController(BlogDAO blogDAO) {
         this.blogDao = blogDAO;
     }
 
     /**
      * Responds to the GET request for a {@linkplain Posts post} for the given id
      * 
      * @param id The id used to locate the {@link Posts post}
      * 
      * @return ResponseEntity with {@link Posts post} object and HTTP status of OK if found<br>
      * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
      * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
      *
      * Command:
      * curl.exe -X GET 'http://localhost:8080/posts/#'
      */
     @GetMapping("/{id}")
     public ResponseEntity<Posts> getPost(@PathVariable int id) {
         LOG.info("GET /posts/" + id);
         try {
             Posts post = blogDao.getPost(id);
             if (post != null)
                 return new ResponseEntity<Posts>(post, HttpStatus.OK);
             else
                 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
         catch(IOException e) {
             LOG.log(Level.SEVERE,e.getLocalizedMessage());
             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }
 
     /**
      * Responds to the GET request for all needs in the cupboard
      * 
      * @return ResponseEntity with array of need objects (may be empty) and
      * HTTP status of OK<br>
      * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
      *
      * Command:
      * curl.exe -X GET 'http://localhost:8080/posts'
      */
     @GetMapping("")
     public ResponseEntity<Posts[]> getPosts(){
         LOG.info("GET /posts");
         {
             try {
                 Posts[] posts = blogDao.getPosts();
                 if (posts != null){
                     return new ResponseEntity<Posts[]>(posts, HttpStatus.OK);
                 } else {
                     return new ResponseEntity<Posts[]>(new Posts[0], HttpStatus.OK);
                 }
             } catch(IOException e) {
                 LOG.log(Level.SEVERE, e.getLocalizedMessage());
                 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
             }
         }   
     }
 
     /**
      * Responds to the GET request in the cupboard for all whose name contains
      * the text in name
      * 
      * @param name The name parameter which contains the text used to find the specificNeed
      * 
      * @return ResponseEntity with array of needs objects (may be empty) and
      * HTTP status of OK<br>
      * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
      * <p>
      * 
      * Command:
      * curl.exe -X GET 'http://localhost:8080/posts/?username=js2004'
      */
     @GetMapping("/")
     public ResponseEntity<Posts[]> searchPost(@RequestParam String username) {
         LOG.info("GET /posts/?username="+username);
         try {
             // Aquire list of needs
             Posts[] posts = blogDao.findPosts(username);
             if (posts != null){
                 return new ResponseEntity<Posts[]>(posts, HttpStatus.OK);
             } else {
                 return new ResponseEntity<Posts[]>(new Posts[0], HttpStatus.OK);
             }
         }
         catch(IOException e) {
             LOG.log(Level.SEVERE,e.getLocalizedMessage());
             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }
 
     /**
      * Creates a post with the provided need object
      * 
      * @param post - The need to create
      * 
      * @return ResponseEntity with created need object and HTTP status of CREATED<br>
      * ResponseEntity with HTTP status of CONFLICT if need object already exists<br>
      * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
      *
      * Commands:
      * curl.exe -X POST -H 'Content-Type:application/json' 'http://localhost:8080/posts' -d '{\"description\": \"\", \"OPUsername\": \"\"}'
      */
     @PostMapping("")
     public ResponseEntity<Posts> createPost(@RequestBody Posts post) {
         LOG.info("POST /posts " + post);
         try {
             //making need
             Posts newPost = blogDao.createPost(post); 
             if (getPost(newPost.getId()) != null) {
                 return new ResponseEntity<Posts>(newPost, HttpStatus.CREATED);
             } else {
                 return new ResponseEntity<Posts>(newPost, HttpStatus.CONFLICT);
             }
         } catch(IOException e) {
             LOG.log(Level.SEVERE,e.getLocalizedMessage());
             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }
 
     /**
      * Updates the post with the provided post object, if it exists
      * 
      * @param post The need to update
      * 
      * @return ResponseEntity with updated need object and HTTP status of OK if updated<br>
      * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
      * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
      *
      * Commands:
      * curl.exe -X PUT -H 'Content-Type:application/json' 'http://localhost:8080/posts' -d '{\"id\": , \"description\": \"\", \"OPUsername\": \"\", \"likes\": }' 
      */
     @PutMapping("")
     public ResponseEntity<Posts> updatePost(@RequestBody Posts post) {  // 
         LOG.info("PUT /posts " + post);
         try {
             Posts updatedPost = blogDao.updatePost(post);
             if (updatedPost != null && updatedPost.getOPUsername() != null){
                 return new ResponseEntity<Posts>(updatedPost, HttpStatus.OK);
             } else {
                 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
             }
         } catch (IOException e){
             LOG.log(Level.SEVERE, e.getLocalizedMessage());
             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }
 
     /**
      * Deletes a post with the given id
      * 
      * @param id The id of the post to deleted
      * 
      * @return ResponseEntity HTTP status of OK if deleted<br>
      * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
      * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
      *
      * Commands:
      * curl.exe -X DELETE ‘http://localhost:8080/posts/#'
      */
     @DeleteMapping("/{id}")
     public ResponseEntity<Posts> deletePost(@PathVariable int id) {
         LOG.info("DELETE /posts/" + id);
         try {
             Boolean exist = blogDao.deletePost(id);
             if (exist)
                 return new ResponseEntity<>(HttpStatus.OK);
             else
                 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
         catch(IOException e) {
             LOG.log(Level.SEVERE,e.getLocalizedMessage());
             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }
 }