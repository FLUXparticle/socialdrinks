package com.example.socialdrinks.feed.entity;

import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.*;

import java.io.*;

@Document(collection = "users")
public class User implements Serializable {

    @Id
    private String id;

    private String username;

    // Konstruktoren, Getter und Setter
    public User() {
        // empty
    }

    public User(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                ", username='" + username + '\'' +
                '}';
    }

}
