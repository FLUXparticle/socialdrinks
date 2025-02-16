package com.example.socialdrinks.feed.model;

import jakarta.validation.constraints.*;

public class SubscribeForm {

    @NotBlank(message = "Username darf nicht leer sein")
    private String creatorName;

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public String toString() {
        return "SubscribeForm{" +
                "creatorName='" + creatorName + '\'' +
                '}';
    }

}
