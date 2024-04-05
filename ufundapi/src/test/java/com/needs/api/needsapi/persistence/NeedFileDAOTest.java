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
import com.needs.api.needsapi.model.Need;
//import com.needs.api.needsapi.persistence.NeedFileDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.Date;

/**
 * Test the Need File DAO class
 * 
 * @author IceCaps
 */
@Tag("Persistence-tier")
public class NeedFileDAOTest {
    NeedFileDAO needFileDAO;
    Need[] testNeeds;
    ObjectMapper mockObjectMapper;

    private Date utilDate = new Date();
    private java.sql.Date date = new java.sql.Date(utilDate.getTime());
    boolean notw = false;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupNeedFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        // {"id" name" "description" "quantity" "price"}
        // quantity cannot be less than or equal to 0
        testNeeds = new Need[3];
        testNeeds[0] = new Need(1,"Volunteer Service", "Seeking volunteers to clean beach", 20, 0.0, notw, date);
        testNeeds[1] = new Need(2,"Monetary Donation", "Seeking donations: funding", 1, 10.0, notw, date);
        testNeeds[2] = new Need(3,"Physical Donation", "Seeking donations: home goods, clothing", 1, 0.0, notw, date);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the need array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Need[].class))
                .thenReturn(testNeeds);
        needFileDAO = new NeedFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetNeeds() {
        // Invoke
        Need[] needs = needFileDAO.getNeeds();
        // Analyze
        assertEquals(needs.length,testNeeds.length);
        for (int i = 0; i < testNeeds.length;++i)
            assertEquals(needs[i],testNeeds[i]);
    }

    @Test
    public void testFindNeeds() {
        // Invoke
        // Finding Monetary/Physical Don-ations
        Need[] needs = needFileDAO.findNeeds("Don");
        // Analyze
        assertEquals(needs.length,2);
        assertEquals(needs[0],testNeeds[1]);
        assertEquals(needs[1],testNeeds[2]);
    }

    @Test
    public void testGetNeed() {
        // get need with id 2
        Need need = needFileDAO.getNeed(2);
        // Analzye
        assertEquals(need,testNeeds[1]);
    }

    @Test
    public void testDeleteNeed() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> needFileDAO.deleteNeed(1),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check deletion status using size
        assertEquals(needFileDAO.needs.size(),testNeeds.length-1);
    }

    @Test
    public void testCreateNeed() {
        // Setup
        Need need = new Need(4,"Tree Llanting", "Seeking volunteers to plant trees", 30, 0.0, notw, date);
        // Invoke
        Need result = assertDoesNotThrow(() -> needFileDAO.createNeed(need),
                                "Unexpected exception thrown");
        // Analyze
        assertNotNull(result);
        Need actual = needFileDAO.getNeed(need.getId());
        assertEquals(actual.getId(),need.getId());
        assertEquals(actual.getName(),need.getName());
    }


    @Test
    public void testUpdateExistingNeed() throws IOException {
        // Setup
        Need need = new Need(4, "Tree Planting", "Seeking volunteers to plant trees", 30, 0.0, notw, date);
        needFileDAO.createNeed(need);

        // Modify the need
        String newName = "Updated Tree Planting";
        need.setName(newName);

        // Invoke
        Need result = assertDoesNotThrow(() -> needFileDAO.updateNeed(need),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Need actual = needFileDAO.getNeed(need.getId());
        assertEquals(newName, actual.getName());
    }

    @Test
    public void testUpdateNonExistingNeed() throws IOException {
        // Setup
        Need need = new Need(999, "Non-existing Need", "Description", 10, 0.0, notw, date);

        // Invoke
        Need result = assertDoesNotThrow(() -> needFileDAO.updateNeed(need),
                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testUpdateIOException() throws IOException {
        // Setup
        Need need = new Need(4, "Tree Planting", "Seeking volunteers to plant trees", 30, 0.0, notw, date);
        needFileDAO.createNeed(need);

        // Mock ObjectMapper to throw IOException during save
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class), any(Need[].class));

        // Invoke and Analyze
        assertThrows(IOException.class,
                        () -> needFileDAO.updateNeed(need),
                        "IOException not thrown");

        // Check if the need remains unchanged
        Need actual = needFileDAO.getNeed(need.getId());
        assertEquals(need, actual);
}


    @Test
    public void testCreateSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Need[].class));

        Need need = new Need(4,"Tree Planting", "Seeking volunteers to plant trees", 30, 0.0, notw, date);
        assertThrows(IOException.class,
                        () -> needFileDAO.createNeed(need),
                        "IOException not thrown");
    }

    @Test
    public void testGetNeedNotFound() {
        // Invoke
        Need need = needFileDAO.getNeed(98);

        // Analyze
        assertEquals(need,null);
    }

    @Test
    public void testDeleteNeedNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> needFileDAO.deleteNeed(98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(needFileDAO.needs.size(),testNeeds.length);
    }

    @Test
    public void testUpdateNeedNotFound() {
        // Setup
        Need need = new Need(98,"Imaginary", null, 0, 0, notw, date);

        // Invoke
        Need result = assertDoesNotThrow(() -> needFileDAO.updateNeed(need),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    // @Test
    // public void testConstructorException() throws IOException {
    //     // Setup
    //     ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
    //     // We want to simulate with a Mock Object Mapper that an
    //     // exception was raised during JSON object deseerialization
    //     // into Java objects
    //     // When the Mock Object Mapper readValue method is called
    //     // from the NeedFileDAO load method, an IOException is
    //     // raised
    //     doThrow(new IOException())
    //         .when(mockObjectMapper)
    //             .readValue(new File("doesnt_matter.txt"),Need[].class);

    //     // Invoke & Analyze
    //     assertThrows(IOException.class,
    //                     () -> new NeedFileDAO("doesnt_matter.txt",mockObjectMapper),
    //                     "IOException not thrown");
    // }
}
