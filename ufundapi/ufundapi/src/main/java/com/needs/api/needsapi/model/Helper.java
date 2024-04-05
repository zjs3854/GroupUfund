package com.needs.api.needsapi.model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.needs.api.needsapi.persistence.NeedFileDAO;
import com.needs.api.needsapi.model.UserItem;
import com.needs.api.needsapi.model.Need;

public class Helper{
    
    // Gets the default username and password for storage and then creates defaults for other functions
    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;
    @JsonProperty("fundbasket") private ArrayList<UserItem> fundbasket;

    // This is the constructor
    public Helper(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("fundbasket") ArrayList<UserItem> fundbasket) {
        // Stores and username and password
        this.username = username;
        this.password = password;
        this.fundbasket = fundbasket;
    }

    /**
     * Returns the password of the user
     * @return String password 
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the fundBasket
     */
    public ArrayList<UserItem> getFundBasket() {
        return basket;
    }

    /**
     * gives a user access to the cupboard
     */
    public Need[] viewCupboard() {
        Need[] cupboard = DAO.getNeeds();
        for (Need need : cupboard) {
            System.out.println(need);
        }
        return cupboard;
    } 

    /**
     * searches the cupboard for a need
     * 
     * @param containsText
     */
    public Need[] searchCupboard(String containsText) {
        Need[] cupboard = DAO.findNeeds(containsText);
        for (Need need : cupboard) {
            System.out.println(need);
        }
        return cupboard;
    }
    
    /**
     * adds a need to the fund basket
     * 
     * @param need
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    public void addToBasket(Need need, int quantity) throws JsonGenerationException, JsonMappingException, IOException {
        UserItem userTemp = new UserItem(need.getId(), quantity);

        basket.add(userTemp);

        mapper.writeValue(fundbasket, basket);
    }

    private int searchBasket(ArrayList<UserItem> basket, int id) {
        for(int i = 0; i < basket.size(); i++) {
            if(basket.get(i).getId() == id) {
                return i;
            }
        }

        return -1;
    }

    /**
     * removes a need from the fund basket
     * 
     * @param need
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     */
    public void removeFromBasket(Need need) throws JsonGenerationException, JsonMappingException, IOException {
        if (!basket.isEmpty()) {
            int indexForRemove = searchBasket(this.basket, need.getId());
            if(indexForRemove >= 0) {
                basket.remove(indexForRemove);
            } else {
                System.out.println("No ID found.");
            }
        }

        mapper.writeValue(fundbasket, basket);
    }

    public void updateNeedQty(int id, int newQuantity){
        int indexToUpdate = searchBasket(basket, id);
        if(indexToUpdate >= 0) {
            basket.get(indexToUpdate).setQuantity(newQuantity);
        } else {
            System.out.println("No ID found.");
        }
    }
}
