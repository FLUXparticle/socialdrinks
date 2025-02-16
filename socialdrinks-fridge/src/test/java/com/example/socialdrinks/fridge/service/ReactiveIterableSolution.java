package com.example.socialdrinks.fridge.service;

import com.example.socialdrinks.model.entity.*;
import org.springframework.web.reactive.function.client.*;

public class ReactiveIterableSolution {

    public static void main(String[] args) {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();

        CocktailRemoteService cocktailRemoteService = new CocktailRemoteService(webClient);

        Iterable<Cocktail> cocktails = cocktailRemoteService.getAllCocktails().toIterable();

        int totalMilkAmount = 0;
        for (Cocktail cocktail : cocktails) {
            String cocktailName = cocktail.getName();
            if (cocktailName.contains("Milk")) {
                System.out.println("Cocktail: " + cocktailName);
                CocktailDetails cocktailDetails = cocktailRemoteService.getCocktailDetails(cocktail.getId()).block();
                for (Instruction instruction : cocktailDetails.getInstructions()) {
                    if ("Milch".equalsIgnoreCase(instruction.getIngredient().getName())) {
                        totalMilkAmount += instruction.getAmountCL();
                    }
                }
            }
        }

        System.out.println("Total amount of 'Milch': " + totalMilkAmount + " cl");
    }

}
