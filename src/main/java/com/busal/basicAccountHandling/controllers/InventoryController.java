package com.busal.basicAccountHandling.controllers;

import java.util.ArrayList;
import java.util.List;

import com.busal.basicAccountHandling.models.Inventory;
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


@RestController
public class InventoryController {

    @Autowired
    InventoryRepository inventoryRepo;

    
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/createInventory")
    public Inventory createInventory(@RequestBody Inventory inventory){
        return inventoryRepo.save(inventory);
    }

    @GetMapping("/displayAllInventory")
    public List<Inventory> getAllInventory(){
        return inventoryRepo.findAll();
    }

    @DeleteMapping("/deleteInventoryByEmail/{email}")
    public ResponseEntity<String> deleteInventoryByEmail(@PathVariable String email) {
        if (inventoryRepo.existsByUserEmail(email)) {
            inventoryRepo.deleteByUserEmail(email);
            return ResponseEntity.ok("User inventory deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User inventory not found");
        }
    }

    @PutMapping("/updateUserInventory/{email}")
    public ResponseEntity<String> updateUserInventory(@RequestBody Inventory updateUserRequest) {
        Inventory existingUser = inventoryRepo.findByUserEmail(updateUserRequest.getUserEmail());
        if (existingUser != null) {
            modelMapper.map(updateUserRequest, existingUser);
            inventoryRepo.deleteByUserEmail(existingUser.getUserEmail());
            inventoryRepo.save(existingUser);
            return ResponseEntity.ok("User inventory updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User inventory not found");
        }
    }

    @GetMapping("/findInventoryByEmail/{email}")
    public ResponseEntity<?> findInventoryByEmail(@PathVariable String email) {
        List<Inventory> inventoryList = new ArrayList<>();
        inventoryList.add(inventoryRepo.findByUserEmail(email));
        if (!inventoryList.isEmpty() && inventoryList.get(0).getUserEmail() != null) {
            return ResponseEntity.ok(inventoryList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User inventory not found");
        }
    }
}
