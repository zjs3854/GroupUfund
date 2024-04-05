package com.needs.api.needsapi.model;

public abstract class User {
    /**
     * Returns the username of the user
     * @return String username 
     */
    public abstract String getUsername();
    
    /**
     * Allows the user to view the cupboard
     */
    public abstract Need[] viewCupboard();
    
    /**
     * Allows the user to search the cupboard
     */
    public abstract Need[] searchCupboard(String containsText);

    /**
     * returns the password of the user
     * @return String password
     */
    public abstract String getPassword();
}
