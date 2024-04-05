package com.needs.api.needsapi.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.needs.api.needsapi.persistence.NeedFileDAO;


public class Helper{
    
    // Gets the default username and password for storage and then creates defaults for other functions
    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;
    @JsonProperty("fundBasket") private ArrayList<UserItem> fundBasket;
    private NeedFileDAO DAO;


    /**
     * Create a Need 
     * 
     * @param username The name of the helper
     * @param string
     * @param string The description of the need
     * @param i The quanitity of what is needed
     * @param price The price of the needed amount
     */
    // This is the constructor
    public Helper(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("fundBasket") ArrayList<UserItem> fundBasket) {
        // Stores and username and password
        this.username = username.toLowerCase();
        this.password = password;
        this.fundBasket = (fundBasket != null) ? fundBasket : new ArrayList<>();
    }
    
    /**
     * Returns the password of the user
     * @return Arraylist fundBasket 
     */
    public ArrayList<UserItem> getfundBasket() {
        return fundBasket;
    }

    public void addToFundBasket(UserItem item) {
        fundBasket.add(item);
    }
    

    
    public void removeFromBasket(UserItem item) {
        int index=0;
        while(index<fundBasket.size()) {
            if(fundBasket.get(index).getId()==item.getId()) {
                fundBasket.remove(index);
                return;
            }
            index++;
        }
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
    
}
