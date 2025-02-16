package com.example.socialdrinks.feed.repository;

import com.example.socialdrinks.feed.entity.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findByCocktailId(Long cocktailId);

    List<Post> findByCocktailNameContaining(String query);

}
