package com.example.socialdrinks.fridge.service;

import com.example.socialdrinks.model.entity.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;

import java.util.*;

@Service
public class CocktailRemoteService {

    private final RestClient restClient;

    public CocktailRemoteService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<Cocktail> getAllCocktails() {
        Cocktail[] response = restClient.get()
                .uri("/api/cocktails")
                .retrieve()
                .body(Cocktail[].class);
        return response == null ? List.of() : Arrays.asList(response);
    }

    public CocktailDetails getCocktailDetails(Long id) {
        return restClient.get()
                .uri("/api/cocktails/{id}", id)
                .retrieve()
                .body(CocktailDetails.class);
    }

    public List<Ingredient> getAllIngredients() {
        Ingredient[] response = restClient.get()
                .uri("/api/ingredients")
                .retrieve()
                .body(Ingredient[].class);
        return response == null ? List.of() : Arrays.asList(response);
    }

    public Ingredient getIngredientWithID(Long id) {
        return restClient.get()
                .uri("/api/ingredients/{id}", id)
                .retrieve()
                .body(Ingredient.class);
    }

    public List<Cocktail> getPossibleCocktails(Collection<Long> ingredientIDs) {
        // Erstelle das Anfrage-Body-Objekt
        Map<String, Collection<Long>> requestBody = new HashMap<>();
        requestBody.put("ingredientIDs", ingredientIDs);

        // Führe den POST-Request aus
        Cocktail[] response = restClient.post()
                .uri("/api/possible")
                .body(requestBody)
                .retrieve()
                .body(Cocktail[].class);
        return response == null ? List.of() : Arrays.asList(response);
    }

}
