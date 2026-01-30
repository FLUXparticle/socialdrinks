package com.example.socialdrinks.cocktails.service;

import com.example.socialdrinks.cocktails.favorite.*;
import com.example.socialdrinks.cocktails.model.*;
import com.example.socialdrinks.model.entity.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final CocktailService cocktailService;

    public FavoriteService(FavoriteRepository favoriteRepository, CocktailService cocktailService) {
        this.favoriteRepository = favoriteRepository;
        this.cocktailService = cocktailService;
    }

    public boolean toggleFavorite(String username, Long cocktailId) {
        if (cocktailId == null || cocktailService.getCocktailWithID(cocktailId) == null) {
            return false;
        }
        String normalized = normalizeUsername(username);
        Optional<Favorite> existing = favoriteRepository.findByUsernameAndCocktailId(normalized, cocktailId);
        if (existing.isPresent()) {
            favoriteRepository.delete(existing.get());
            return false;
        }
        favoriteRepository.save(new Favorite(normalized, cocktailId, new Date()));
        return true;
    }

    public List<FavoriteCocktailDTO> getCocktailOverview() {
        Map<Long, Long> counts = getFavoriteCounts();
        return cocktailService.getAllCocktails().stream()
                .sorted(Comparator.comparing(Cocktail::getName))
                .map(cocktail -> new FavoriteCocktailDTO(
                        cocktail.getId(),
                        cocktail.getName(),
                        false,
                        counts.getOrDefault(cocktail.getId(), 0L)
                ))
                .toList();
    }

    public List<FavoriteCocktailDTO> getFavoritesOverview(String username) {
        String normalized = normalizeUsername(username);
        Set<Long> favorites = favoriteRepository.findByUsername(normalized).stream()
                .map(Favorite::getCocktailId)
                .collect(Collectors.toSet());
        Map<Long, Long> counts = getFavoriteCounts();

        return cocktailService.getAllCocktails().stream()
                .sorted(Comparator.comparing(Cocktail::getName))
                .map(cocktail -> new FavoriteCocktailDTO(
                        cocktail.getId(),
                        cocktail.getName(),
                        favorites.contains(cocktail.getId()),
                        counts.getOrDefault(cocktail.getId(), 0L)
                ))
                .toList();
    }

    private Map<Long, Long> getFavoriteCounts() {
        return favoriteRepository.findAll().stream()
                .map(Favorite::getCocktailId)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(id -> id, Collectors.counting()));
    }

    private String normalizeUsername(String username) {
        if (username == null || username.isBlank()) {
            return "anonymous";
        }
        return username;
    }
}
