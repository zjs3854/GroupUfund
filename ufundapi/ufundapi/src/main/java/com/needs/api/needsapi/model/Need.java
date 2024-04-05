package com.needs.api.needsapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Need entity
 * 
 * @author IceCaps
 */
public class Need {
    private static final Logger LOG = Logger.getLogger(Need.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Need [id=%d, name=%s, desc=%s, quantity=%d, price=%f]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("description") private String description; 
    @JsonProperty("quantity") private int quantity;
    @JsonProperty("price") private double price;

    /**
     * Create a Need 
     * 
     * @param name The name of the need
     * @param string
     * @param string The description of the need
     * @param i The quanitity of what is needed
     * @param price The price of the needed amount
     */
    public Need(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("description") String d, 
    @JsonProperty("quantity") int q, @JsonProperty("price") double price) {
        this.id = id;
        this.name = name;
        this.description = d;
        this.quantity = q;
        this.price = price; 
    }

    /**
     * Retrieves the id of the need
     * @return The id of the need
     */
    public int getId() {return id;}

    /**
     * Sets the name of the need - necessary for JSON object to Java object deserialization
     * @param name The name of the need
     */
    public void setName(String name) {this.name = name;}

    /**
     * Retrieves the name of the need
     * @return The name of the need
     */
    public String getName() {return name;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String formattedString = String.format(STRING_FORMAT, id, name, description, quantity, price);
        return formattedString;
    }

     /**
     * Setter for the description of the need
     */
    public void setDescription(String newDesc){
        this.description = newDesc;
    }
    /**
     * Getter for the description of the need
     * 
     * @return The description of the need
     */
    public String getDescription(){
        return this.description;
    }
    /**
     * Setter for the quantity of the need
     */
    public void setQuantity(int newQuantity){
        this.quantity = newQuantity;
    }
    /**
     * Getter for the quantity of the need
     * 
     * @return The quantity of the need
     */
    public int getQuantity(){
        return this.quantity;
    }
    /**
     * Setter for the price of the need
     */
    public void setPrice(double newPrice){
        this.price = newPrice;
    }
    /**
     * Getter for the price of the need
     * 
     * @return The price of the need
     */
    public double getPrice(){
        return this.price;
    }
}