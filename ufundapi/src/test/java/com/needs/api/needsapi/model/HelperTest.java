package com.needs.api.needsapi.model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;


public class HelperTest {
    private Helper helper;
    
    @BeforeEach
    public void setUpHelper() {
        helper = new Helper("name","password", null);
    }

    @Test
    public void getPasswordTest() {
        //Setup
        String expected = "password";
        //Invoke
        String actual = helper.getPassword();
        //Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void getUsernameTest() {
        //Setup
        String expected = "name";
        //Invoke
        String actual = helper.getUsername();
        //Analyze
        assertEquals(expected, actual);
    }
    
    @Test
    public void getFundBasketTest() {
        //Setup
        ArrayList<UserItem> expected = new ArrayList<UserItem>();
        
        
        UserItem zero = new UserItem(0, 0);
        UserItem one = new UserItem(1,1);
        UserItem two = new UserItem(2,2);
        expected.add(zero);
        expected.add(one);
        expected.add(two);

        //Invoke        
        helper.addToFundBasket(zero);
        helper.addToFundBasket(one);
        helper.addToFundBasket(two);
        
        
        ArrayList<UserItem> actual = helper.getfundBasket();
        //Analyze
        assertEquals(expected, actual);
    }


    //covers this.fundBasket = (fundBasket != null) ? fundBasket : new ArrayList<>();
    @Test
    public void constructorTest() {
        // Setup
        ArrayList<UserItem> expected = new ArrayList<>();
        expected.add(new UserItem(0, 0));
        // Invoke
        Helper helper = new Helper("name", "password", expected);
        // Analyze
        assertEquals(expected, helper.getfundBasket());
    }


    @Test
    public void addToBasketTest() {
        //Setup
        UserItem expected = new UserItem(0,1);
        try {
            helper.addToFundBasket(expected);
        }
        catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("Exception!");
        }
        //Invoke
        UserItem actual = helper.getfundBasket().get(0);
        //Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void removeFromBasketTest() {
        //Setup
        Helper helper = new Helper("username", "password", null);
        UserItem one = new UserItem(1, 1);
        // only add one
        helper.addToFundBasket(one);
        //Invoke and Analyze
        helper.removeFromBasket(one); 
        // basket remains unchanged
        assertEquals(0, helper.getfundBasket().size());
    }

    @Test
    public void removeFromBasketNotFoundTest() {
        //Setup
        Helper helper = new Helper("username", "password", null);
        UserItem notFound = new UserItem(2, 1);
        UserItem one = new UserItem(1, 1);
        // only add one
        helper.addToFundBasket(one);

        //Invoke and Analyze
        helper.removeFromBasket(notFound); 
        
        // basket remains unchanged
        assertEquals(1, helper.getfundBasket().size());
        assertEquals(one, helper.getfundBasket().get(0));
    }

    @Test
    void testSearchCupboard() {
    }

    @Test
    void testViewCupboard() {

    }
}

