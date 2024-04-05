package com.needs.api.needsapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single Post entity
 * 
 * @author IceCaps
 */

public class Posts {
    private static final Logger LOG = Logger.getLogger(Posts.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Post [id=%d, description=%s, OPUsername=%s, likes=%d]";

    @JsonProperty("id") private int id;
    @JsonProperty("description") private String description;
    @JsonProperty("OPUsername") private String OPUsername;
    @JsonProperty("likes") private int likes;
    @JsonProperty("userLikes") private String[] userLikes;

    /**
     * Create a post
     * 
     * @param desc The posts main content
     * @param OP This stands for original poster. This will be the username of the poster
     * @param like The number of likes this post has
     */
    public Posts(@JsonProperty("id") int id, @JsonProperty("description") String desc, @JsonProperty("OPUsername") String OP, @JsonProperty("likes") int likes,@JsonProperty("userLikes") String[] userLikes) {
        this.id = id;
        this.description = desc;
        this.OPUsername = OP;
        this.likes = likes;
        this.userLikes=userLikes;
    }

    /**
     * Retrieves the id of the post
     * @return The id of the post
     */
    public int getId() { return id; }

    /**
     * Retrieves the description of the post
     * @return The description of the post
     */
    public String getDescription() { return description; }

    /**
     * Allows the description to be changed whenever wanted
     * @param edits the newly changed description
     */
    public void setDescription(String edits) { this.description = edits; }

    /**
     * Retrieves the username of the poster of the post
     * @return The username of the poster
     */
    public String getOPUsername() { return OPUsername; }

    /**
     * Retrieves the amoutn of likes the post has
     * @return The number of likes the post has
     */
    public int getLikes() { return likes; }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String formattedString = String.format(STRING_FORMAT, id, description, OPUsername, likes);
        return formattedString;
    }
}
