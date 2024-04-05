package com.needs.api.needsapi.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.needs.api.needsapi.model.Helper;

import com.needs.api.needsapi.persistence.UserDAO;

/**
 * Test the Authentication Controller class
 * 
 * @author IceCaps
 */
@Tag("Controller-tier")
public class AuthenticationControllerTest {
    private AuthenticationController authenticationController;
    private UserDAO mockUserDAO;

    /**
     * Before each test, create a new AuthenticationController object and inject
     * a mock Helper DAO
     */
    @BeforeEach
    public void setupAuthenticationController() {
        mockUserDAO = mock(UserDAO.class);
        authenticationController = new AuthenticationController(mockUserDAO);
    }

    // GET USER TESTS

    @Test
    public void testGetUser() throws IOException {  // getUser may throw IOException
        // Setup
        Helper helper = new Helper("Dawn","passwrd",null);
        String searchString = "Dawn";
        // When the same id is passed in, our mock Helper DAO will return the Helper object
        when(mockUserDAO.getUser(helper.getUsername(),"passwrd")).thenReturn(helper);

        // Invoke
        ResponseEntity<Helper> response = authenticationController.getUser(searchString,"passwrd");

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(helper,response.getBody());
    }

    
    @Test
    public void testGetUserNotFound() throws Exception { // createUser may throw IOException
        // Setup
        String name = "Jim";
        String pass = "Err";
        // When the same id is passed in, our mock Helper DAO will return null, simulating
        // no helper found
        when(mockUserDAO.getUser(name,pass)).thenReturn(null);

        // Invoke
        ResponseEntity<Helper> response = authenticationController.getUser(name,pass);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    void testGetUserInternalServerError() throws IOException {
    // Setup
    String name = "UserWithException";
    String passwrd = "passwrd";

    // Mocking HelperDAO to throw IOException
    doThrow(new IOException()).when(mockUserDAO).getUser(name.toLowerCase(), passwrd);

    // Invoke
    ResponseEntity<Helper> response = authenticationController.getUser(name, passwrd);

    // Analyze
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    // GET USERS PLURAL TESTS

    @Test
    void testGetUsers() throws IOException {
        // Setup
        Helper[] helpers = {
            new Helper("User1", "passwrd1", null),
            new Helper("User2", "passwrd2", null)
        };

        // Mocking the behavior of UserDAO to return the array of helpers
        when(mockUserDAO.getUsersArray()).thenReturn(helpers);

        // Invoke
        ResponseEntity<Helper[]> response = authenticationController.getUsers();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(helpers, response.getBody());
    }

    @Test
    void testGetUsersNotFound() throws IOException {
        // Mocking the behavior of UserDAO to return null, simulating no helpers found
        when(mockUserDAO.getUsersArray()).thenReturn(null);

        // Invoke
        ResponseEntity<Helper[]> response = authenticationController.getUsers();

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetUsersInternalServerError() throws IOException {
        // Mocking UserDAO to throw IOException
        doThrow(new IOException()).when(mockUserDAO).getUsersArray();

        // Invoke
        ResponseEntity<Helper[]> response = authenticationController.getUsers();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    // CREATE USER TESTS

    @Test
    public void testCreateUser() throws IOException {  // createUser may throw IOException
        // Setup
        Helper helper = new Helper("Global Warming","passwrd",null);
        // when createUser is called, return true simulating successful
        // creation and save
        when(mockUserDAO.createUser(helper)).thenReturn(helper);

        // Invoke
        ResponseEntity<Helper> response = authenticationController.createUser(helper);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(helper,response.getBody());
    }

    @Test
    public void testCreateUserConflict() throws IOException {  // createUser may throw IOException
        // Setup
        Helper helper = new Helper("Wild Fire","passwrd",null);
        // when createUser is called, return false simulating failed
        // creation and save
        when(mockUserDAO.createUser(helper)).thenReturn(null);

        // Invoke
        ResponseEntity<Helper> response = authenticationController.createUser(helper);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateUserInternalServerError() throws IOException {  // createUser may throw IOException
        // Setup
        Helper helper = new Helper("Tsunami","Passwrd",null);

        // When createUser is called on the Mock Helper DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).createUser(helper);

        // Invoke
        ResponseEntity<Helper> response = authenticationController.createUser(helper);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    void testCreateUserAdmin() throws IOException {
        // Setup
        Helper adminUser = new Helper("admin", "adminPasswrd", null);

        // Invoke
        ResponseEntity<Helper> response = authenticationController.createUser(adminUser);

        // Analyze
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // UPDATE USER TESTS
    
    @Test
    void testUpdateHelper() throws IOException {  
        // Setup
        Helper updatedHelper = new Helper("UpdatedHelper", "newPasswrd", null);

        // Mocking the behavior of UserDAO to return the updated Helper
        when(mockUserDAO.updateHelper(updatedHelper)).thenReturn(updatedHelper);

        // Invoke
        ResponseEntity<Helper> response = authenticationController.updateHelper(updatedHelper);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedHelper, response.getBody());
    }

    @Test
    void testUpdateHelperNotFound() throws IOException {  
        // Setup
        Helper updatedHelper = new Helper("NonExistingHelper", "newPasswrd", null);

        // Mocking the behavior of UserDAO to return null, simulating no Helper found
        when(mockUserDAO.updateHelper(updatedHelper)).thenReturn(null);

        // Invoke
        ResponseEntity<Helper> response = authenticationController.updateHelper(updatedHelper);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateHelperInternalServerError() throws IOException {  
        // Setup
        Helper updatedHelper = new Helper("ErrorHelper", "newPasswrd", null);

        // Mocking UserDAO to throw IOException
        doThrow(new IOException()).when(mockUserDAO).updateHelper(updatedHelper);

        // Invoke
        ResponseEntity<Helper> response = authenticationController.updateHelper(updatedHelper);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}