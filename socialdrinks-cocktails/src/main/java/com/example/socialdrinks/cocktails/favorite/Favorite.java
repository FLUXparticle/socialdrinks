package com.example.socialdrinks.cocktails.favorite;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.*;

@Document(collection = "favorites")
public class Favorite {

    @Id
    private String id;

    private String username;

    private Long cocktailId;

    private Date createdAt;

    public Favorite() {
        // empty
    }

    public Favorite(String username, Long cocktailId, Date createdAt) {
        this.username = username;
        this.cocktailId = cocktailId;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Long getCocktailId() {
        return cocktailId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
