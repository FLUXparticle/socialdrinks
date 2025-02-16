package com.example.socialdrinks.feed.entity;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.*;

import java.io.*;
import java.util.*;

@Document(collection = "posts")
public class Post implements Serializable {

    @Id
    private String id;

    @DBRef
    private User creator;

    private Date createdAt;

    private Long cocktailId;

    private String cocktailName;

    private Integer rating;

    // Konstruktoren, Getter und Setter
    public Post() {
        // empty
    }

    public Post(User creator, Date createdAt, Long cocktailId, String cocktailName, Integer rating) {
        this.creator = creator;
        this.createdAt = createdAt;
        this.cocktailId = cocktailId;
        this.cocktailName = cocktailName;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCocktailId() {
        return cocktailId;
    }

    public void setCocktailId(Long cocktailId) {
        this.cocktailId = cocktailId;
    }

    public String getCocktailName() {
        return cocktailName;
    }

    public void setCocktailName(String cocktailName) {
        this.cocktailName = cocktailName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Post{");
        sb.append("creator=").append(creator.getUsername());
        sb.append(", createdAt=").append(createdAt);
//        sb.append(", cocktailId=").append(cocktailId);
        sb.append(", cocktailName='").append(cocktailName).append('\'');
        sb.append(", rating=").append(rating);
        sb.append('}');
        return sb.toString();
    }

}
