package com.busal.basicAccountHandling.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.busal.basicAccountHandling.models.Subscription;

public interface SubscriptionRespository extends MongoRepository<Subscription,String>{
    void deleteByCustomerEmail(String customerEmail);
    boolean existsByCustomerEmail(String customerEmail);
    Subscription findByCustomerEmail(String customerEmail);
}