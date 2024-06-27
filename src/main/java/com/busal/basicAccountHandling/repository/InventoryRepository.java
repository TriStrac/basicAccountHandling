package com.busal.basicAccountHandling.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.busal.basicAccountHandling.models.Inventory;

public interface InventoryRepository extends MongoRepository<Inventory,String>{
    void deleteByEmail(String email);
    boolean existsByEmail(String email);
    Inventory findByEmail(String email);
}
