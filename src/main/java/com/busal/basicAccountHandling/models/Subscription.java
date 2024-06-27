package com.busal.basicAccountHandling.models;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "subscriptionsAquaroute")
public class Subscription {
    private String customerEmail;
    private String stationEmail;
    
    public Subscription() {
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    public String getStationEmail() {
        return stationEmail;
    }
    public void setStationEmail(String stationEmail) {
        this.stationEmail = stationEmail;
    }

}
