package com.needs.api.needsapi.model;

public class UserItem {
    private int id;
    private int quantity;

    public UserItem(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

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
}
