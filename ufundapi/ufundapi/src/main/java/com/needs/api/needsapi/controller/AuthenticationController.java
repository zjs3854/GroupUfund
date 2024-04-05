package com.needs.api.needsapi.controller;

// Not used in this but could be at some point (Maybe)
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.needs.api.needsapi.persistence.NeedDAO;
import com.needs.api.needsapi.persistence.UserDAO;
import com.needs.api.needsapi.model.Helper;
import com.needs.api.needsapi.model.Need;

/**
 * Handles the REST API requests for the Need resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author IceCaps
 */
@RestController
@RequestMapping("users")
public class AuthenticationController {
    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private UserDAO userDao;

     /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param userDAO The {@link NeedDAO Need Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     * @return 
     */
    public  AuthenticationController(UserDAO userDao) {
        this.userDao= userDao;
    }
    
    @GetMapping("/")
    public ResponseEntity<Helper> getUser(@RequestParam String name,@RequestParam String password) {
        LOG.info("GET /users/?name="+name+"&password="+password);
        try {
            name=name.toLowerCase();
            Helper helper=userDao.getUser(name,password);
            if(helper !=null){
                return new ResponseEntity<Helper>(helper, HttpStatus.OK);
            }
            else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Creates a {@linkplain User helper} with the provided user object
     * 
     * @param helper - The {@link User helper} to create
     * 
     * @return ResponseEntity with created {@link Need need} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Need need} object already exists or if the username is empty<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Helper> createUser(@RequestBody Helper helper) {
        LOG.info("POST /users" + helper);
        System.out.println(":3");
        try {
            if(helper.getUsername() == "admin") {
                return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
            } else {
                Helper user= userDao.createUser(helper);
                if(user != null)
                    return new ResponseEntity<Helper>(user, HttpStatus.CREATED);
                else
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
