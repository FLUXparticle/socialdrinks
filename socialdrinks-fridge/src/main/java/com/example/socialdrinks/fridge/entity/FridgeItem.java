package com.example.socialdrinks.fridge.entity;

import org.springframework.data.annotation.Id;

public class FridgeItem {

    @Id
    private Long id;

    private String username;

    private Long ingredientId;

    protected FridgeItem() {
    }

    public FridgeItem(Long id, String username, Long ingredientId) {
        this.id = id;
        this.username = username;
        this.ingredientId = ingredientId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }
}
