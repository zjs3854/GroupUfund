package com.needs.api.needsapi.persistence;

import java.io.IOException;

import com.needs.api.needsapi.model.Helper;

public interface UserDAO {
    /**
     * Creates and saves a {@linkplain Helper user}
     * 
     * @param helper {@linkplain Helper user} object to be created and saved
     * <br>
     * The id of the need object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Helper user} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Helper createUser(Helper helper) throws IOException;

    /**
     * Retrieves a {@linkplain Helper user} with the given id
     * 
     * @param username The username of the {@link Helper user} to get
     * 
     * @return a {@link Helper user} object with the matching id
     * <br>
     * null if no {@link Helper user} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Helper getUser(String username, String password) throws IOException;
    
}