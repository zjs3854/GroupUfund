package com.needs.api.needsapi.persistence;

import java.io.IOException;
import java.util.ArrayList;

import com.needs.api.needsapi.model.Helper;
import com.needs.api.needsapi.model.Need;

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
     * Retrieves all {@linkplain Helper helpers}
     * 
     * @return An array of {@link Helper} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Helper[] getUsersArray() throws IOException;
        
    
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
    
    
    /**
     * Updates and saves a {@linkplain Helper need}
     * 
     * @param {@link Helper need} object to be updated and saved
     * 
     * @return updated {@link Helper need} if successful, null if
     * {@link Helper need} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Helper updateHelper(Helper helper) throws IOException;
}