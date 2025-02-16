package com.example.socialdrinks.fridge.service;

import com.example.socialdrinks.model.entity.*;
import org.springframework.stereotype.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.*;

import java.util.*;

@Service
public class CocktailRemoteService {

    private final WebClient webClient;

    public CocktailRemoteService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<Cocktail> getAllCocktails() {
        return webClient.get()
                .uri("/api/cocktails")
                .retrieve()
                .bodyToFlux(Cocktail.class);
    }

    public Mono<CocktailDetails> getCocktailDetails(Long id) {
        return webClient.get()
                .uri("/api/cocktails/" + id)
                .retrieve()
                .bodyToMono(CocktailDetails.class);
    }

    public Flux<Ingredient> getAllIngredients() {
        return webClient.get()
                .uri("/api/ingredients")
                .retrieve()
                .bodyToFlux(Ingredient.class);
    }

    public Mono<Ingredient> getIngredientWithID(Long id) {
        return webClient.get()
                .uri("/api/ingredients/" + id)
                .retrieve()
                .bodyToMono(Ingredient.class);
    }

    public Flux<Cocktail> getPossibleCocktails(Collection<Long> ingredientIDs) {
        // Erstelle das Anfrage-Body-Objekt
        Map<String, Collection<Long>> requestBody = new HashMap<>();
        requestBody.put("ingredientIDs", ingredientIDs);

        // Führe den POST-Request aus
        return webClient.post()
                .uri("/api/possible")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(Cocktail.class);
    }

}
