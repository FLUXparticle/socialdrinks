package com.example.socialdrinks.feed.repository;

import com.example.socialdrinks.feed.entity.*;
import org.springframework.data.mongodb.repository.*;

import java.util.*;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);

    List<User> findByUsernameIn(List<String> usernames);

}
