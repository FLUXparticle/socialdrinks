package com.example.socialdrinks.fridge.service;

import com.example.socialdrinks.model.entity.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.*;

public class ReactiveFluxSolution {

    public static void main(String[] args) {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();

        CocktailRemoteService cocktailRemoteService = new CocktailRemoteService(webClient);

        long start = System.currentTimeMillis();

        int totalMilkAmount = cocktailRemoteService.getAllCocktails()
                .filter(cocktail -> cocktail.getName().contains("Milk")) // Nur Cocktails mit "Milk" im Namen
                .doOnNext(cocktail -> System.out.println("= " + cocktail.getName()))
                .flatMap(cocktail -> cocktailRemoteService.getCocktailDetails(cocktail.getId()))
                .doOnNext(cocktailDetails -> System.out.println("< " + cocktailDetails.getName()))
                .flatMap(cocktailDetails -> Flux.fromIterable(cocktailDetails.getInstructions()))
                .filter(instruction -> "Milch".equalsIgnoreCase(instruction.getIngredient().getName()))
                .map(Instruction::getAmountCL)
                .reduce(Integer::sum) // Summe aller Milch-Mengen berechnen
                .blockOptional() // Warten, bis der gesamte Prozess abgeschlossen ist
                .orElse(0);

        long stop = System.currentTimeMillis();

        System.out.println("Total milk amount: " + totalMilkAmount);
        System.out.println("Total milk time: " + (stop - start));
    }

}
