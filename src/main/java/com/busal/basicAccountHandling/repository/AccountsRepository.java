package com.busal.basicAccountHandling.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.busal.basicAccountHandling.models.UserAccount;

public interface AccountsRepository extends MongoRepository<UserAccount,String>{
    void deleteByEmail(String email);
    boolean existsByEmail(String email);
    UserAccount findByEmail(String email);
}
