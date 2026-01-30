package com.example.socialdrinks.cocktails.controller;

import com.example.socialdrinks.cocktails.model.*;
import com.example.socialdrinks.cocktails.service.*;
import com.example.socialdrinks.model.entity.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.Collections.*;

@RestController
@RequestMapping("/api")
public class CocktailRestController {

    private final CocktailService cocktailService;
    private final CartService cartService;
    private final FavoriteService favoriteService;

    public CocktailRestController(CocktailService cocktailService, CartService cartService, FavoriteService favoriteService) {
        this.cocktailService = cocktailService;
        this.cartService = cartService;
        this.favoriteService = favoriteService;
    }

    @GetMapping(path = "/cocktails")
    public Collection<Cocktail> getAllCocktails() {
        return cocktailService.getAllCocktails().stream()
                .sorted(Comparator.comparing(Cocktail::getName))
                .toList();
    }

    @GetMapping("/cocktails/{id}")
    public Map<String, Object> cocktail(@PathVariable Long id) {
        Cocktail cocktail = cocktailService.getCocktailWithID(id);

        return Map.of(
                "name", cocktail.getName(),
                "instructions", cocktail.getInstructions()
        );
    }

    @GetMapping("/cocktails/search")
    public Collection<Cocktail> searchCocktails(@RequestParam String query) {
        return cocktailService.search(query);
    }

    @PostMapping("/possible")
    public Collection<Cocktail> getPossibleRecipes(@RequestBody Map<String, List<Long>> payload) {
        List<Long> ingredientIDs = payload.getOrDefault("ingredientIDs", emptyList());
        return cocktailService.getPossibleCocktails(new HashSet<>(ingredientIDs));
    }

    @GetMapping("/ingredients")
    public Collection<Ingredient> getAllIngredients() {
        return cocktailService.getAllIngredients();
    }

    @GetMapping("/ingredients/{id}")
    public Ingredient getIngredient(@PathVariable Long id) {
        return cocktailService.getIngredientWithID(id);
    }

    @GetMapping("/ingredients/{id}/cocktails")
    public Map<String, Object> ingredient(@PathVariable Long id) {
        Ingredient ingredient = cocktailService.getIngredientWithID(id);
        Collection<Cocktail> cocktails = cocktailService.getAllCocktailsWithIngredient(id);
        return Map.of(
                "name", ingredient.getName(),
                "cocktails", cocktails
        );
    }

    @GetMapping("/cart")
    public Map<String, Object> getCart() {
        return Map.of(
                "ingredients", cartService.getIngredients(),
                "cocktails", cartService.getCocktailsForSelection()
        );
    }

    @PostMapping("/cart/actions/add-cocktail")
    public void addCocktailToCart(@RequestBody Map<String, Long> payload) {
        Long cocktailId = payload.get("cocktailId");
        if (cocktailId != null) {
            cartService.addCocktail(cocktailId);
        }
    }

    @DeleteMapping("/cart/items/{ingredientId}")
    public void removeIngredientFromCart(@PathVariable Long ingredientId) {
        cartService.removeIngredient(ingredientId);
    }

    @DeleteMapping("/cart")
    public void clearCart() {
        cartService.clear();
    }

    @GetMapping("/favorites")
    public Collection<FavoriteCocktailDTO> getFavoritesOverview(
            @RequestHeader(value = "X-User", required = false) String username
    ) throws InterruptedException {
        Thread.sleep(1000);
        return favoriteService.getFavoritesOverview(username);
    }

    @PostMapping("/favorites/toggle")
    public Map<String, Object> toggleFavorite(
            @RequestHeader(value = "X-User", required = false) String username,
            @RequestBody Map<String, Long> payload
    ) throws InterruptedException {
        Thread.sleep(1000);
        Long cocktailId = payload.get("cocktailId");
        boolean favorite = cocktailId != null && favoriteService.toggleFavorite(username, cocktailId);
        return Map.of("favorite", favorite);
    }

}
