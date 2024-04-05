package com.needs.api.needsapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.needs.api.needsapi.model.Need;
import com.needs.api.needsapi.persistence.NeedDAO;
import java.util.Date;

/**
 * Test the Need Controller class
 * 
 * @author IceCaps
 */
@Tag("Controller-tier")
public class NeedControllerTest {
    private NeedController needController;
    private NeedDAO mockNeedDAO;
    private Date utilDate = new Date();
    private java.sql.Date date = new java.sql.Date(utilDate.getTime());

    /**
     * Before each test, create a new NeedController object and inject
     * a mock Need DAO
     */
    @BeforeEach
    public void setupNeedController() {
        mockNeedDAO = mock(NeedDAO.class);
        needController = new NeedController(mockNeedDAO);
    }

    // GET NEED TESTS

    @Test
    public void testGetNeed() throws IOException {  // getNeed may throw IOException
        // Setup
        Need need = new Need(99,"Rainforest Rehab", "Help Donate to works trying to reforest the rainforest", 400, 10, false, date);
        // When the same id is passed in, our mock Need DAO will return the Need object
        when(mockNeedDAO.getNeed(need.getId())).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(need.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }


    @Test
    public void testGetNeedNotFound() throws Exception { // createNeed may throw IOException
        // Setup
        int needId = 99;
        // When the same id is passed in, our mock Need DAO will return null, simulating
        // no need found
        when(mockNeedDAO.getNeed(needId)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetNeedInternalServerError() throws Exception { // createNeed may throw IOException
        // Setup
        int needId = 99;
        // When getNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).getNeed(needId);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    // CREATE NEED TESTS

    @Test
    public void testCreateNeed() throws IOException {
        // Setup
        Need need = new Need(200, "Space Scanning", "Help fund to scan new forest areas", 600, 100, false, date);
        // Ensure that the DAO returns null to simulate no conflict
        when(mockNeedDAO.createNeed(need)).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test
    public void testCreateNeedNull() throws IOException {
        // Setup
        Need need = new Need(45, "Space Scanning", "Help fund to scan new forest areas", 600, 100, false, date);
        // When createNeed is called on the Mock Need DAO, return null, simulating
        // a conflict
        when(mockNeedDAO.createNeed(need)).thenReturn(null);
    
        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);
    
        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
    }
    
    @Test
    public void testCreateNeedQuantityZero() throws IOException {
        // Setup
        Need need = new Need(45, "Space Scanning", "Help fund to scan new forest areas", 0, 100, false, date);
    
        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);
    
        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode()); // Change to BAD_REQUEST
        assertNull(response.getBody());
    }
    
    @Test
    public void testCreateNeedQuantityNeg() throws IOException {
        // Setup
        Need need = new Need(45, "Space Scanning", "Help fund to scan new forest areas", -1, 100, false, date);
    
        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);
    
        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode()); // Change to BAD_REQUEST
        assertNull(response.getBody());
    }
    
    @Test
    public void testCreateNeedInternalServerError() throws IOException {
        // Setup
        Need need = new Need(45, "Space Scanning", "Help fund to scan new forest areas", 600, 100, false, date);
        // When createNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).createNeed(need);
    
        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);
    
        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    
    // partial unit test / combined test for createNeed with existing need with the same name and ID
    // can separate into two tests later
    @Test
    public void testCreateNeedExisting() throws IOException {
        // Setup
        Need existingNeedWithName = new Need(5, "Existing Need", "Description", 300, 10, false, date);
        Need existingNeedWithId = new Need(6, "New Need", "Description", 400, 10, false, date);
        // Simulate existing need with the same name and ID
        when(mockNeedDAO.findNeeds(existingNeedWithName.getName())).thenReturn(new Need[]{existingNeedWithName});
        when(mockNeedDAO.getNeed(existingNeedWithId.getId())).thenReturn(existingNeedWithId);

        // Invoke
        ResponseEntity<Need> responseName = needController.createNeed(existingNeedWithName);
        ResponseEntity<Need> responseId = needController.createNeed(existingNeedWithId);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, responseName.getStatusCode());
        assertNull(responseName.getBody());

        assertEquals(HttpStatus.CONFLICT, responseId.getStatusCode());
        assertNull(responseId.getBody());
    }

    // UPDATE NEED TESTS

    @Test
    public void testUpdateNeed() throws IOException {
        // Setup
        Need need = new Need(45, "Space Scanning", "Help fund to scan new forest areas", 600, 100, false, date);
        when(mockNeedDAO.updateNeed(need)).thenReturn(need);

        ResponseEntity<Need> response = needController.updateNeed(need);
        need.setName("Bolt");

        // Invoke
        response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(need, response.getBody());
    }

    @Test
    public void testUpdateNeedNotFound() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(45,"Space Scanning", "Help fund to scan new forest areas", 600, 100, false, date);
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockNeedDAO.updateNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateNeedInternalServerError() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(45,"Space Scanning", "Help fund to scan new forest areas", 600, 100, false, date);
        // When updateNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).updateNeed(need);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    // update need edge cases : check for quantity < 0 and quantity equal to 0
    @Test
    public void testUpdateNeedQuantityZero() throws IOException {
        // Setup
        Need need = new Need(45, "Space Scanning", "Help fund to scan new forest areas", 0, 0, false, date);
        when(mockNeedDAO.updateNeed(need)).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        
    }

    @Test
    public void testUpdateNeedQuantityNeg() throws IOException {
        // Setup
        Need need = new Need(45, "Space Scanning", "Help fund to scan new forest areas", -1, 0, false, date);
        when(mockNeedDAO.updateNeed(need)).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // GET NEEDS PLURAL TESTS

    @Test
    public void testGetNeeds() throws IOException { // getNeeds may throw IOException
        // Setup
        Need[] needs = new Need[2];
        needs[0] = new Need(45,"Space Scanning", "Help fund to scan new forest areas", 600, 100, false, date);
        needs[1] = new Need(68,"Wild-Fire", "Fires are very hot", 12, 14, false, date);
        // When getNeeds is called return the needs created above
        when(mockNeedDAO.getNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());
    }


    @Test
    public void testGetNeedsNull() throws IOException {
        // Setup
        when(mockNeedDAO.getNeeds()).thenReturn(null);

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // an empty array instead of null
        assertEquals(0, response.getBody().length); 
    }


    @Test
    public void testGetNeedsInternalServerError() throws IOException { // getNeeds may throw IOException
        // Setup
        // When getNeeds is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).getNeeds();

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    // SEARCH NEEDS TESTS

    @Test
    public void testSearchNeeds() throws IOException { // findNeeds may throw IOException
        // Setup
        String searchString = "Fire";
        Need[] needs = new Need[2];
        needs[0] = new Need(45,"Space Fire Scanning", "Help fund to scan new forest areas", 600, 100, false, date);
        needs[1] = new Need(68,"Wild Fire", "Fires are very hot", 12, 14, false, date);
        // When findNeeds is called with the search string, return the two
        /// needs above
        when(mockNeedDAO.findNeeds(searchString)).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeed(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());
    }

    @Test
    public void testSearchNeedsInternalServerError() throws IOException { // findNeeds may throw IOException
        // Setup
        String searchString = "an";
        doThrow(new IOException()).when(mockNeedDAO).findNeeds(searchString);

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeed(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchNeedsNull() throws IOException {
        // Setup
        String searchString = "Fire";
        when(mockNeedDAO.findNeeds(searchString)).thenReturn(null);

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeed(searchString);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().length);
    }

    // DELETE NEED TESTS

    @Test
    public void testDeleteNeed() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 99;
        // when deleteNeed is called return true, simulating successful deletion
        when(mockNeedDAO.deleteNeed(needId)).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 99;
        // when deleteNeed is called return false, simulating failed deletion
        when(mockNeedDAO.deleteNeed(needId)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedInternalServerError() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 99;
        // When deleteNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).deleteNeed(needId);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
