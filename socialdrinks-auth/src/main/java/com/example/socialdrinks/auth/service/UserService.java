package com.example.socialdrinks.auth.service;

import org.springframework.stereotype.*;

import java.util.*;

@Service
public class UserService {

    private static final String PASSWORD = "Secret123";

    private final Map<String, String> users = Map.of(
            "employee", "USER",
            "manager", "USER",
            "admin", "ADMIN",
            "helpdesk", "HELPDESK"
            );

    public String getRole(String username, String password) {
        String role = users.get(username);

        if (role != null && password.equals(PASSWORD)) {
            return role;
        }

        return null;
    }

    static class User {
        private String username;
        private String password;
        private String role;
    }

}
