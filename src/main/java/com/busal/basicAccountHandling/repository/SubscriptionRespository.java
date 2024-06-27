package com.busal.basicAccountHandling.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.busal.basicAccountHandling.models.Subscription;

public interface SubscriptionRespository extends MongoRepository<Subscription,String>{
    void deleteByEmail(String email);
    boolean existsByEmail(String email);
    Subscription findByEmail(String email);
}