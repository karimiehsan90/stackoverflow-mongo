package com.journaldev.bootifulmongodb.dal;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.journaldev.bootifulmongodb.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public User getByEmail(String email);

    public User getByEmailAndPassword(String email, String password);

    public User getByUserId(String id);
}
