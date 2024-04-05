package com.needs.api.needsapi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserItem {
    private int id;
    private int quantity;

    @JsonCreator
    public UserItem(@JsonProperty("id") int id,@JsonProperty("quantity") int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    // Package private for tests
    static final String STRING_FORMAT = "UserItem [id=%d, quantity=%d]";

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int newQuantity){
        this.quantity = newQuantity;
    }

    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String formattedString = String.format(STRING_FORMAT, id, quantity);
        return formattedString;
    }
}
