package com.busal.basicAccountHandling.controllers;

import java.util.ArrayList;
import java.util.List;

import com.busal.basicAccountHandling.models.Inventory;
import com.busal.basicAccountHandling.models.Subscription;
import com.busal.basicAccountHandling.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class SubscriptionController {
    @Autowired
    SubscriptionRespository subscriptionRepo;
    
    @Autowired
    private ModelMapper modelMapper;


    @PostMapping("/createSubscription")
    public Subscription createSubscription(@RequestBody Subscription subscription){
        return subscriptionRepo.save(subscription);
    }

    @GetMapping("/displayAllSubscriptions")
    public List<Subscription> getAllSubscriptions(){
        return subscriptionRepo.findAll();
    }

    @DeleteMapping("/deleteSubscriptionByEmail/{email}")
    public ResponseEntity<String> deleteSubscriptionByEmail(@PathVariable String email) {
        if (subscriptionRepo.existsByCustomerEmail(email)) {
            subscriptionRepo.deleteByCustomerEmail(email);
            return ResponseEntity.ok("User subscription deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User subscription not found");
        }
    }

    @PutMapping("/updateUserSubscription/{email}")
    public ResponseEntity<String> updateUserSubscription(@RequestBody Subscription updateUserRequest) {
        Subscription existingUser = subscriptionRepo.findByCustomerEmail(updateUserRequest.getCustomerEmail());
        if (existingUser != null) {
            modelMapper.map(updateUserRequest, existingUser);
            subscriptionRepo.deleteByCustomerEmail(existingUser.getCustomerEmail());
            subscriptionRepo.save(existingUser);
            return ResponseEntity.ok("User subscription updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User subscription not found");
        }
    }

    @GetMapping("/findSubscriptionByEmail/{email}")
    public ResponseEntity<?> findSubscriptionByEmail(@PathVariable String email) {
        List<Subscription> inventoryList = new ArrayList<>();
        inventoryList.add(subscriptionRepo.findByCustomerEmail(email));
        if (!inventoryList.isEmpty() && inventoryList.get(0).getCustomerEmail() != null) {
            return ResponseEntity.ok(inventoryList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User subscription not found");
        }
    }

}
