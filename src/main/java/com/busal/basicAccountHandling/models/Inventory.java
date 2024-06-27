package com.busal.basicAccountHandling.models;

public class Inventory {
    private String userEmail;
    private Float gallons;

    
    public Inventory() {
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public Float getGallons() {
        return gallons;
    }
    public void setGallons(Float gallons) {
        this.gallons = gallons;
    }
}
