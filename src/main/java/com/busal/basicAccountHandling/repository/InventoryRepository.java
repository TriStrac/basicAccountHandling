package com.busal.basicAccountHandling.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.busal.basicAccountHandling.models.Inventory;

public interface InventoryRepository extends MongoRepository<Inventory,String>{
    void deleteByUserEmail(String userEmail);
    boolean existsByUserEmail(String userEmail);
    Inventory findByUserEmail(String userEmail);
}
