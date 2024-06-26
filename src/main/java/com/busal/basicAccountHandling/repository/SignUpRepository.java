package com.busal.basicAccountHandling.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.busal.basicAccountHandling.models.UserAccount;

public interface SignUpRepository extends MongoRepository<UserAccount,String>{

}
