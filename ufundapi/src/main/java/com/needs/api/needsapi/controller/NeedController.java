package com.needs.api.needsapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.needs.api.needsapi.persistence.NeedDAO;
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
@RequestMapping("needs")
public class NeedController {
    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());
    private NeedDAO needDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param needDao The {@link NeedDAO Need Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public NeedController(NeedDAO needDao) {
        this.needDao = needDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Need need} for the given id
     * 
     * @param id The id used to locate the {@link Need need}
     * 
     * @return ResponseEntity with {@link Need need} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id) {
        LOG.info("GET /needs/" + id);
        try {
            Need need = needDao.getNeed(id);
            if (need != null)
                return new ResponseEntity<Need>(need,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private Need getNeed(String name) {
         Need[] needs = searchNeed(name).getBody();
        if(needs!=null) {
             for(Need n: needs) {
                 if(n.getName().equals(name)) {
                     return n;
                 }
             }
         }
         return null;
    }

    /**
     * Responds to the GET request for all needs in the cupboard
     * 
     * @return ResponseEntity with array of need objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Need[]> getNeeds(){
        LOG.info("GET /needs");
        {
            try {
                Need[] needs = needDao.getNeeds();
                if (needs != null){
                    return new ResponseEntity<Need[]>(needs, HttpStatus.OK);
                } else {
                    return new ResponseEntity<Need[]>(new Need[0], HttpStatus.OK);
                }
            } catch(IOException e) {
                LOG.log(Level.SEVERE, e.getLocalizedMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }   
    }

    /**
     * Responds to the GET request in the cupboard for all whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the specificNeed
     * 
     * @return ResponseEntity with array of needs objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: GET http://localhost:8080/needs/?name= " _______ "
     */
    @GetMapping("/")
    public ResponseEntity<Need[]> searchNeed(@RequestParam String name) {
        LOG.info("GET /needs/?name="+name);
        try {
            // Aquire list of needs
            Need[] needs = needDao.findNeeds(name);
            if (needs != null){
                return new ResponseEntity<Need[]>(needs, HttpStatus.OK);
            } else {
                return new ResponseEntity<Need[]>(new Need[0], HttpStatus.OK);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//DO NOT MESS WITH THIS OR THE WHOLE THING BREAKS. PLEASE.
    /**
     * Creates a need with the provided need object
     * 
     * @param need - The need to create
     * 
     * @return ResponseEntity with created need object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if need object already exists or quantity is <= 0 <br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */@PostMapping("")
    public ResponseEntity<Need> createNeed(@RequestBody Need need) {
        LOG.info("POST /needs " + need);
        try {
            Need newNeed = needDao.createNeed(need);
            // if the new need is successful: CREATED, otherwise CONFLICT
            if(getNeed(newNeed.getId())!=null && getNeed(newNeed.getName())!=null && newNeed.getQuantity()>0) {
                return new ResponseEntity<Need>(newNeed,HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity<Need>(newNeed,HttpStatus.CONFLICT);
            }
        } catch(IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Updates the need with the provided need object, if it exists
     * 
     * @param need The need to update
     * 
     * @return ResponseEntity with updated need object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Need> updateNeed(@RequestBody Need need) {  // 
        LOG.info("PUT /needs " + need);
        try {
            Need updatedNeed = needDao.updateNeed(need);
            if (updatedNeed != null && updatedNeed.getQuantity()>0){
                return new ResponseEntity<Need>(updatedNeed, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a need with the given id
     * 
     * @param name The id of the need to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Need> deleteNeed(@PathVariable int id) {
        LOG.info("DELETE /needs/" + id);
        try {
            Boolean  exist = needDao.deleteNeed(id);
            if (exist)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
