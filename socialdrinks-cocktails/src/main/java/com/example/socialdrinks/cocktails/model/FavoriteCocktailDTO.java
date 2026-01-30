package com.example.socialdrinks.cocktails.model;

public class FavoriteCocktailDTO {

    private Long id;
    private String name;
    private boolean favorite;
    private long favoriteCount;

    public FavoriteCocktailDTO(Long id, String name, boolean favorite, long favoriteCount) {
        this.id = id;
        this.name = name;
        this.favorite = favorite;
        this.favoriteCount = favoriteCount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public long getFavoriteCount() {
        return favoriteCount;
    }
}
