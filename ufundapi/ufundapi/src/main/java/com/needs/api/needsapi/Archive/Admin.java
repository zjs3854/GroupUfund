package com.needs.api.needsapi.model;

import java.io.IOException;

import com.needs.api.needsapi.persistence.NeedFileDAO;

public class Admin{
    private NeedFileDAO DAO;
    private String password = "password";

    public Admin(String password) {
        this.password = password;
    }

    /**
     * Returns the username of the user
     * @return String username 
     */
    public String getUsername() {
        return "admin";
    }

    /**
     * gives a user access to the cupboard
     * @return 
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
     * @return 
     */
    public Need[] searchCupboard(String containsText) {
        Need[] cupboard = DAO.findNeeds(containsText);
        for (Need need : cupboard) {
            System.out.println(need);
        }
        return cupboard;   
    }

    /**
     * returns the password of the user
     * @return String password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * adds a need to the cupboard
     * 
     * @param need
     * @throws IOException 
     */
    public void addNeed(Need need) throws IOException {
        DAO.createNeed(need);
    }

    /**
     * updates a need in the cupboard
     * 
     * @param need
     * @throws IOException 
     */
    public void updateNeed(Need need) throws IOException {
        DAO.updateNeed(need);
    }

    /**
     * removes a need from the cupboard
     * 
     * @param need
     * @throws IOException 
     */
    public void removeNeed(Need need) throws IOException {
        DAO.deleteNeed(need.getId());
    }
}
