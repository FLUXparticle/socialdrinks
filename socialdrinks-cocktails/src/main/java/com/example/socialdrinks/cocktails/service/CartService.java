package com.example.socialdrinks.cocktails.service;

import com.example.socialdrinks.model.entity.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.*;
import org.springframework.web.context.annotation.*;

import java.util.*;

@Service
@SessionScope
public class CartService {

    private final CocktailService cocktailService;
    private final Set<Long> ingredientIds = new LinkedHashSet<>();

    public CartService(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    public void addCocktail(Long cocktailId) {
        Cocktail cocktail = cocktailService.getCocktailWithID(cocktailId);
        if (cocktail == null) {
            return;
        }
        for (Instruction instruction : cocktail.getInstructions()) {
            Ingredient ingredient = instruction.getIngredient();
            if (ingredient != null && ingredient.getId() != null) {
                ingredientIds.add(ingredient.getId());
            }
        }
    }

    public void removeIngredient(Long ingredientId) {
        ingredientIds.remove(ingredientId);
    }

    public void clear() {
        ingredientIds.clear();
    }

    public List<Ingredient> getIngredients() {
        return ingredientIds.stream()
                .map(cocktailService::getIngredientWithID)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Ingredient::getName))
                .toList();
    }

    public List<Cocktail> getCocktailsForSelection() {
        return cocktailService.getAllCocktails().stream()
                .sorted(Comparator.comparing(Cocktail::getName))
                .toList();
    }

}
