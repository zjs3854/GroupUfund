package com.needs.api.needsapi.persistence;

import java.io.IOException;

import com.needs.api.needsapi.model.Posts;

/**
 * Defines the interface for Posts object persistence
 * 
 * @author IceCaps
 */
public interface BlogDAO {
    /**
     * Retrieves all {@linkplain Posts posts}
     * 
     * @return An array of {@link Posts posts} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Posts[] getPosts() throws IOException;

    /**
     * Finds all {@linkplain Posts posts} whose name contains the given text
     * 
     * @param username The text to match against
     * 
     * @return An array of {@link Posts postss} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Posts[] findPosts(String username) throws IOException;

    /**
     * Retrieves a {@linkplain Posts post} with the given id
     * 
     * @param id The id of the {@link Posts post} to get
     * 
     * @return a {@link Posts post} object with the matching id
     * <br>
     * null if no {@link Posts post} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Posts getPost(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain Posts post}
     * 
     * @param post {@linkplain Posts post} object to be created and saved
     * <br>
     * The id of the need object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Posts post} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Posts createPost(Posts post) throws IOException;

    /**
     * Updates and saves a {@linkplain Posts post}
     * 
     * @param {@link Posts post} object to be updated and saved
     * 
     * @return updated {@link Posts post} if successful, null if
     * {@link Posts post} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Posts updatePost(Posts post) throws IOException;

    /**
     * Deletes a {@linkplain Posts post} with the given id
     * 
     * @param id The id of the {@link Posts post}
     * 
     * @return true if the {@link Posts post} was deleted
     * <br>
     * false if need with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deletePost(int id) throws IOException;
}
