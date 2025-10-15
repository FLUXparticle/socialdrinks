package com.example.socialdrinks.cocktails.controller;

import com.example.socialdrinks.cocktails.service.*;
import com.example.socialdrinks.model.entity.*;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.Collections.*;

@RestController
@RequestMapping("/api")
public class CocktailRestController {

    private final CocktailService cocktailService;

    public CocktailRestController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @GetMapping(path = "/cocktails")
    public Collection<Cocktail> getAllCocktails() throws InterruptedException {
        Thread.sleep(1000);
        return cocktailService.getAllCocktails().stream()
                .sorted(Comparator.comparing(Cocktail::getName))
                .toList();
    }

    @GetMapping("/cocktails/{id}")
    public Map<String, Object> cocktail(@PathVariable Long id) throws InterruptedException {
        Thread.sleep(1000);
        Cocktail cocktail = cocktailService.getCocktailWithID(id);
        String averageRating = Optional.ofNullable(cocktailService.getAverageRating(id))
                .map("%.1f"::formatted)
                .orElse("");

        return Map.of(
                "name", cocktail.getName(),
                "instructions", cocktail.getInstructions(),
                "averageRating", averageRating
        );
    }

    @GetMapping("/cocktails/search")
    public Collection<Cocktail> searchCocktails(@RequestParam String query) throws InterruptedException {
        Thread.sleep(1000);
        return cocktailService.search(query);
    }

    public static class PossibleRequest {
        @NotNull
        List<Long> ingredientIDs;

        public List<Long> getIngredientIDs() {
            return ingredientIDs;
        }

        public void setIngredientIDs(List<Long> ingredientIDs) {
            this.ingredientIDs = ingredientIDs;
        }
    }

    @PostMapping("/possible")
    public Map<String, Object> getPossibleRecipes(@RequestBody @Valid PossibleRequest payload, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            return Map.of("errors", errors);
        }

        List<Long> ingredientIDs = payload.getIngredientIDs();
        List<Cocktail> possibleCocktails = cocktailService.getPossibleCocktails(new HashSet<>(ingredientIDs));
        return Map.of("cocktails", possibleCocktails);
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

}
