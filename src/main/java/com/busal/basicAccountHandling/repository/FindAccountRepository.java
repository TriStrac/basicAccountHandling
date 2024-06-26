package com.busal.basicAccountHandling.repository;

import java.util.List;

import com.busal.basicAccountHandling.models.UserAccount;

public interface FindAccountRepository {

    List<UserAccount> findByEmail(String text);
}
