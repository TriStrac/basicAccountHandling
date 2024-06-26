package com.busal.basicAccountHandling.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import com.busal.basicAccountHandling.models.UserAccount;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
public class FindAccountRepositoryImplement implements FindAccountRepository {

    @Autowired
    MongoClient client;

    @Autowired
    MongoConverter converter;

    public List<UserAccount> findByEmail(String text){

        final List<UserAccount> accounts = new ArrayList<>();

        MongoDatabase database = client.getDatabase("sysarchDemo");
        MongoCollection<Document> collection = database.getCollection("accounts");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search", 
        new Document("index", "accountsDefault")
            .append("text", 
        new Document("query", text)
                .append("path", "email"))), 
        new Document("$limit", 1L)));

        result.forEach(doc -> accounts.add(converter.read(UserAccount.class, doc)));

        return accounts;
    }
}
