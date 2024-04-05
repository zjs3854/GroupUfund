package com.needs.api.needsapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.needs.api.needsapi.model.Posts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for Blog
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author IceCaps
 */

@Component
public class BlogFileDAO implements BlogDAO {
    private static final Logger LOG = Logger.getLogger(BlogFileDAO.class.getName());
    Map<Integer, Posts> posts; // Provides a local cache of the need objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper; // Provides conversion between Need
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId; // The next ID to assign to a new post
    private String filename; // Filename to read from and write to

    /**
     * Creates a Posts File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides a JSON Object to read/write from Java Object serialization
     * 
     * @throws IOException when a file cannot be accessed of read from
     */
    public BlogFileDAO(@Value("${posts.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load(); // Load the needs from the file
    }

    /**
     * Generates the next id for a new post
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of Posts from the tree map
     * @return The created array, This array could be empty
     */
    private Posts[] getPostsArray() {
        return getPostsArray(null);
    }

    /**
     * Generates an array of {@linkplain Posts posts} from the tree map for any
     * {@linkplain Posts posts} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Posts posts}
     * in the tree map
     * 
     * @return  The array of {@link Posts posts}, may be empty
     */
    private Posts[] getPostsArray(String username) {
        ArrayList<Posts> postArrayList = new ArrayList<>();

        for (Posts post : posts.values()) {
            if (username == null || post.getOPUsername().contains(username)) {
                postArrayList.add(post);
            }
        }

        Posts[] postArray = new Posts[postArrayList.size()];
        postArrayList.toArray(postArray);
        return postArray;
    }

    /**
     * Saves the {@linkplain Posts posts} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Posts posts} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Posts[] postArray = getPostsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename), postArray);
        return true;
    }

    /**
     * Loads {@linkplain Posts posts} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        posts = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of needs
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Posts[] postArray = objectMapper.readValue(new File(filename), Posts[].class);

        // Add each need to the tree map and keep track of the greatest id
        for (Posts post : postArray) {
            posts.put(post.getId(), post);
            if (post.getId() > nextId)
                nextId = post.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Posts[] getPosts() {
        synchronized(posts) {
            return getPostsArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Posts[] findPosts(String containsText) {
        synchronized(posts) {
            return getPostsArray(containsText);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Posts getPost(int id) {
        synchronized(posts) {
            if (posts.containsKey(id))
                return posts.get(id);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Posts createPost(Posts post) throws IOException {
        synchronized(posts) {
            Posts newPost = new Posts(nextId(), post.getDescription(), post.getOPUsername(), 0, null);
            posts.put(newPost.getId(), newPost);
            save();
            return newPost;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Posts updatePost(Posts post) throws IOException {
        synchronized(posts) {
            if (posts.containsKey(post.getId()) == false)
                return null;  // need does not exist

            posts.put(post.getId(), post);
            save(); // may throw an IOException
            return post;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deletePost(int id) throws IOException {
        synchronized(posts) {
            if (posts.containsKey(id)) {
                posts.remove(id);
                return save();
            }
            else
                return false;
        }
    }
}
