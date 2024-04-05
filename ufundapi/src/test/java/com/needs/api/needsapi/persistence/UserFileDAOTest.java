// package com.needs.api.needsapi.persistence;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.when;

// import java.io.File;
// import java.io.IOException;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.needs.api.needsapi.model.Helper;


// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Tag;
// import org.junit.jupiter.api.Test;

// /**
//  * Test the User File DAO class
//  * 
//  * @author Team Ice Caps
//  */
// @Tag("Persistence-tier")
// public class UserFileDAOTest {
//     UserFileDAO userFileDAO;
//     Helper[] testUsers;
//     ObjectMapper mockObjectMapper;

//     /**
//      * Before each test, we will create and inject a Mock Object Mapper to
//      * isolate the tests from the underlying file
//      * @throws IOException
//      */
//     @BeforeEach
//     public void setupUserFileDAO() throws IOException {
//         mockObjectMapper = mock(ObjectMapper.class);
//         testUsers = new Helper[2];
//         testUsers[0] = new Helper("helper", "password",null); // Fix: Create a new instance of the User class
//         testUsers[1] = new Helper("user", "password",null);

//         // When the object mapper is supposed to read from the file
//         // the mock object mapper will return the user array above
//         when(mockObjectMapper
//             .readValue(new File("doesnt_matter.txt"),Helper[].class))
//                 .thenReturn(testUsers);
//         userFileDAO = new UserFileDAO("doesnt_matter.txt",mockObjectMapper);
//     }
// /*
//     @Test
//     public void testGetUser() throws IOException{
//         // Setup
//         String username = "helper";
//         String password = "password";
//         Helper expectedUser = testUsers[1];

//         // Invoke
//         Helper actualUser = userFileDAO.getUser(username,password);

//         // Analyze
//         assertEquals(expectedUser,actualUser);
//     }
//     */
    
// /* 
//     @Test
//     public void testCreateUser() throws IOException {
//         // Setup
//         Helper newUser = new Helper("newUser","password",null);
        
//         // Invoke
//         Helper userActual = userFileDAO.createUser(newUser);

//         // Analyze
//         assertEquals(newUser,userActual);
//     }
// */
//     @Test
//     public void testHasUser() {
//         // Setup
//         String username = "helper";

//         // Invoke
//         boolean hasUser = userFileDAO.hasUser(username);

//         // Analyze
//         assertEquals(true,hasUser);
//     }

// }
