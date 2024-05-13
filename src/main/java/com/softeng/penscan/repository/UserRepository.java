package com.softeng.penscan.repository;

import com.softeng.penscan.model.User;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Object findByUsername(String username);

    List<User> findByUserType(String userType);
}
