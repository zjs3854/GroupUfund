package com.needs.api.needsapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Need class
 * 
 * @author IceCaps
 */
@Tag("Model-tier")
public class NeedTest {

    // Setup
    int expected_id = 42;
    String expected_name = "Chernyshev Ice Cap Fund";
    String expected_description = "description";
    int expected_quantity = 1;
    double expected_price = 1.1;
    boolean expected_notw = false;
    // date
    Date utilDate = new Date();
    java.sql.Date date = new java.sql.Date(utilDate.getTime());

    @Test
    public void testCtor() {
        Need need = new Need(expected_id,expected_name,expected_description,expected_quantity,expected_price,expected_notw,date);

        // Analyze
        assertEquals(expected_id,need.getId());
        assertEquals(expected_name,need.getName());
    }

    @Test
    public void testName() {
        // Setup
        int id = 42;
        String name = "Reusable Plastics";
        Need need = new Need(id,name,expected_description,expected_quantity,expected_price,expected_notw,date);

        String expected_name = "Forest Fire";

        // Invoke
        need.setName(expected_name);

        // Analyze
        assertEquals(expected_name,need.getName());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 42;
        String name = "Reusable Plastics";
        String expected_string = String.format(Need.STRING_FORMAT,id,name,"description",1,1.1);
        Need need = new Need(id,name,"description",1,1.1, false, date);
        // Invoke
        String actual_string = need.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }

    @Test
    void testGetDescription() {
        Need need = new Need(expected_id, expected_name, expected_description, expected_quantity, expected_price, expected_notw, date);
        assertEquals(expected_description, need.getDescription());
    }

    @Test
    void testGetExpireDate() {
        Need need = new Need(expected_id, expected_name, expected_description, expected_quantity, expected_price, expected_notw, date);
        assertEquals(date, need.getExpireDate());
    }

    @Test
    void testGetId() {
        Need need = new Need(expected_id, expected_name, expected_description, expected_quantity, expected_price, expected_notw, date);
        assertEquals(expected_id, need.getId());
    }

    @Test
    void testGetNOTW() {
        Need need = new Need(expected_id, expected_name, expected_description, expected_quantity, expected_price, expected_notw, date);
        assertEquals(expected_notw, need.getNOTW());
    }

    @Test
    void testGetName() {
        Need need = new Need(expected_id, expected_name, expected_description, expected_quantity, expected_price, expected_notw, date);
        assertEquals(expected_name, need.getName());
    }

    @Test
    void testGetPrice() {
        Need need = new Need(expected_id, expected_name, expected_description, expected_quantity, expected_price, expected_notw, date);
        assertEquals(expected_price, need.getPrice());
    }

    @Test
    void testGetQuantity() {
        Need need = new Need(expected_id, expected_name, expected_description, expected_quantity, expected_price, expected_notw, date);
        assertEquals(expected_quantity, need.getQuantity());
    }

    @Test
    void testSetDescription() {
        Need need = new Need(expected_id, expected_name, expected_description, expected_quantity, expected_price, expected_notw, date);
        String newDescription = "New Description";
        need.setDescription(newDescription);
        assertEquals(newDescription, need.getDescription());
    }

    @Test
    void testSetExpireDate() {
        Need need = new Need(expected_id, expected_name, expected_description, expected_quantity, expected_price, expected_notw, date);
        Date newDate = new Date();
        java.sql.Date newSqlDate = new java.sql.Date(newDate.getTime());
        need.setExpireDate(newSqlDate);
        assertEquals(newSqlDate, need.getExpireDate());
    }

    @Test
    void testSetNOTW() {
        Need need = new Need(expected_id, expected_name, expected_description, expected_quantity, expected_price, expected_notw, date);
        boolean newValue = true;
        need.setNOTW(newValue);
        assertEquals(newValue, need.getNOTW());
    }

    @Test
    void testSetName() {
        Need need = new Need(expected_id, expected_name, expected_description, expected_quantity, expected_price, expected_notw, date);
        String newName = "New Name";
        need.setName(newName);
        assertEquals(newName, need.getName());
    }

    @Test
    void testSetPrice() {
        Need need = new Need(expected_id, expected_name, expected_description, expected_quantity, expected_price, expected_notw, date);
        double newPrice = 2.2;
        need.setPrice(newPrice);
        assertEquals(newPrice, need.getPrice());
    }

    @Test
    void testSetQuantity() {
        Need need = new Need(expected_id, expected_name, expected_description, expected_quantity, expected_price, expected_notw, date);
        int newQuantity = 2;
        need.setQuantity(newQuantity);
        assertEquals(newQuantity, need.getQuantity());
    }
}