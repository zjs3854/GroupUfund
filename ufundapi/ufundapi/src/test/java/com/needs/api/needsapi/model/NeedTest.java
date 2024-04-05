package com.needs.api.needsapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.needs.api.needsapi.model.Need;

/**
 * The unit test suite for the Need class
 * 
 * @author IceCaps
 */
@Tag("Model-tier")
public class NeedTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 42;
        String expected_name = "Chernyshev Ice Cap Fund";

        // Invoke
        Need need = new Need(expected_id,expected_name, expected_name, expected_id, expected_id);

        // Analyze
        assertEquals(expected_id,need.getId());
        assertEquals(expected_name,need.getName());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String name = "Fire";
        Need need = new Need(id,name, name, id, id);

        String expected_name = "Forest Fire";

        // Invoke
        need.setName(expected_name);

        // Analyze
        assertEquals(expected_name,need.getName());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 64;
        String name = "Reusable Plastics";
        String expected_string = String.format(Need.STRING_FORMAT,id,name);
        Need need = new Need(id,name, expected_string, id, id);

        // Invoke
        String actual_string = need.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}