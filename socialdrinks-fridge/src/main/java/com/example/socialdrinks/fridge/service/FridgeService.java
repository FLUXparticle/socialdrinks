package com.example.socialdrinks.fridge.service;

import com.example.socialdrinks.fridge.model.*;
import com.example.socialdrinks.model.entity.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;
import reactor.core.publisher.*;

import java.time.*;
import java.util.*;

@Service
public class FridgeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FridgeService.class);

    private final CocktailRemoteService cocktailService;

    @Autowired
    public FridgeService(CocktailRemoteService cocktailService) {
        this.cocktailService = cocktailService;
    }

    // Methode zum Abrufen aller Zutaten im Kühlschrank
    public Flux<FridgeIngredient> getFridgeIngredients() {
        return getFridge()
                .flatMapMany(fridge -> cocktailService.getAllIngredients()
                        .map(ingredient -> {
                            Long ingredientId = ingredient.getId();
                            String name = ingredient.getName();
                            boolean inFridge = fridge.contains(ingredientId);
                            return new FridgeIngredient(ingredientId, name, inFridge);
                        })
                );
    }

    // Methode zum Aktualisieren des Status einer Zutat im Kühlschrank
    public Mono<Void> updateIngredientStatus(Long ingredientId, boolean inFridge) {
        return getFridge().doOnNext(fridge -> {
            if (inFridge) {
                fridge.add(ingredientId);
            } else {
                fridge.remove(ingredientId);
            }
        }).then(); // `then()` macht die Methode zu `Mono<Void>`
    }

    // Methode zum Abrufen möglicher Cocktails basierend auf den Zutaten im Kühlschrank
    public Flux<Cocktail> getPossibleCocktails() {
        return getFridge()
                .flatMapMany(fridge -> fridge.isEmpty()
                        ? Flux.empty()
                        : cocktailService.getPossibleCocktails(fridge)
                );
    }

    public Flux<String> mixBlock(Long cocktailId) {
        return Flux.<String>create(sink -> {
            Mono<CocktailDetails> detailsMono = cocktailService.getCocktailDetails(cocktailId);

            CocktailDetails details = detailsMono.block();

            for (Instruction instruction : details.getInstructions()) {
                sink.next(instruction.toString());
            }

            sink.next("Schütteln...");
            sink.next("Fertig!");
        }).delayElements(Duration.ofSeconds(1));
    }

    public Flux<String> mix(Long cocktailId) {
        Mono<CocktailDetails> detailsMono = cocktailService.getCocktailDetails(cocktailId);

        return detailsMono.flatMapIterable(CocktailDetails::getInstructions)
                .map(Instruction::toString)
                .concatWithValues("Schütteln...", "Fertig!")
                .delayElements(Duration.ofSeconds(1));
    }

    private static Mono<Set<Long>> getFridge() {
        return ReactiveSecurityContextHolder.getContext().map(context -> {
            Authentication authentication = context.getAuthentication();
            return authentication.getName();
        }).switchIfEmpty(Mono.just("anonymous")).map(username -> {
            LOGGER.info("User '{}'", username);
            return allFridges.computeIfAbsent(username, key -> new HashSet<>());
        });
    }

    static final Map<String, Set<Long>> allFridges = new HashMap<>();

    public Flux<String> milkSummary() {
        return cocktailService.getAllCocktails()
                .filter(cocktail -> cocktail.getName().contains("Milk"))
                .flatMap(cocktail -> cocktailService.getCocktailDetails(cocktail.getId()))
                .flatMapIterable(cocktailDetails -> cocktailDetails.getInstructions())
                .filter(instruction -> instruction.getIngredient().getName().equals("Milch"))
                .map(Instruction::getAmountCL)
                .reduce(Integer::sum)
                .flux()
                .map(String::valueOf)
                .delayElements(Duration.ofSeconds(1));
    }

}
