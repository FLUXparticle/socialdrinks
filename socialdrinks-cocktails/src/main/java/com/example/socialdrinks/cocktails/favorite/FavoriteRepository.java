package com.example.socialdrinks.cocktails.favorite;

import org.springframework.data.mongodb.repository.*;

import java.util.*;

public interface FavoriteRepository extends MongoRepository<Favorite, String> {

    Optional<Favorite> findByUsernameAndCocktailId(String username, Long cocktailId);

    List<Favorite> findByUsername(String username);

}
