package com.busal.basicAccountHandling.models;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inventoryAquaroute")
public class Inventory {
    private String userEmail;
    private String gallons;

    
    public Inventory() {
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getGallons() {
        return gallons;
    }
    public void setGallons(String gallons) {
        this.gallons = gallons;
    }
}
