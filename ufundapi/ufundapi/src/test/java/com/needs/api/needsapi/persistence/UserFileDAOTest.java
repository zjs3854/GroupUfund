package com.needs.api.needsapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.needs.api.needsapi.model.Admin;
import com.needs.api.needsapi.model.Helper;
import com.needs.api.needsapi.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the User File DAO class
 * 
 * @author Team Ice Caps
 */
@Tag("Persistence-tier")
public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    User[] testUsers;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupUserFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testUsers = new User[3];
        testUsers[0] = new Admin("admin");
        testUsers[1] = new Helper("helper", "password"); // Fix: Create a new instance of the User class
        testUsers[2] = new Helper("user", "pass");

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the user array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),User[].class))
                .thenReturn(testUsers);
        userFileDAO = new UserFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetUser() throws IOException{
        // Setup
        String username = "helper";
        User expectedUser = testUsers[1];

        // Invoke
        User actualUser = userFileDAO.getUser(username);

        // Analyze
        assertEquals(expectedUser,actualUser);
    }

    @Test
    public void testCreateUser() throws IOException {
        // Setup
        User newUser = new Helper("newUser","password");
        
        // Invoke
        User userActual = userFileDAO.createUser(newUser);

        // Analyze
        assertEquals(newUser,userActual);
    }

    @Test
    public void testGetAdmin() {
        // Invoke
        User admin = userFileDAO.getAdmin();

        // Analyze
        assertNotNull(admin);
        assertEquals("admin",admin.getUsername());
    }

    @Test
    public void testHasUser() {
        // Setup
        String username = "helper";

        // Invoke
        boolean hasUser = userFileDAO.hasUser(username);

        // Analyze
        assertEquals(true,hasUser);
    }

}
