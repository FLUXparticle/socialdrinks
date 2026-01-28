package com.example.socialdrinks.fridge.service;

import com.example.socialdrinks.fridge.model.*;
import com.example.socialdrinks.model.entity.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;

import java.util.*;
import java.util.stream.*;

@Service
public class FridgeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FridgeService.class);

    private final CocktailRemoteService cocktailService;

    private final Map<String, Set<Long>> fridges = new HashMap<>();

    @Autowired
    public FridgeService(CocktailRemoteService cocktailService) {
        this.cocktailService = cocktailService;
    }

    // Methode zum Abrufen aller Zutaten im Kühlschrank
    public List<FridgeIngredient> getFridgeIngredients() {
        Set<Long> fridge = getFridge();
        return cocktailService.getAllIngredients().stream()
                .map(ingredient -> {
                    Long ingredientId = ingredient.getId();
                    String name = ingredient.getName();
                    boolean inFridge = fridge.contains(ingredientId);
                    return new FridgeIngredient(ingredientId, name, inFridge);
                })
                .toList();
    }

    // Methode zum Aktualisieren des Status einer Zutat im Kühlschrank
    public void updateIngredientStatus(Long ingredientId, boolean inFridge) {
        Set<Long> fridge = getFridge();
        if (inFridge) {
                fridge.add(ingredientId);
        } else {
                fridge.remove(ingredientId);
        }
    }

    // Methode zum Abrufen möglicher Cocktails basierend auf den Zutaten im Kühlschrank
    public List<Cocktail> getPossibleCocktails() {
        Set<Long> fridge = getFridge();
        return fridge.isEmpty()
                ? List.of()
                : cocktailService.getPossibleCocktails(fridge);
    }

    public List<String> mix(Long cocktailId) {
        CocktailDetails details = cocktailService.getCocktailDetails(cocktailId);
        List<String> steps = details.getInstructions().stream()
                .map(Instruction::toString)
                .collect(Collectors.toList());
        steps.add("Schütteln...");
        steps.add("Fertig!");
        return steps;
    }

    private Set<Long> getFridge() {
        String username = currentUsername();
        return fridges.computeIfAbsent(username, k -> new HashSet<>());
    }

    private String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? "anonymous" : authentication.getName();
        LOGGER.info("User '{}'", username);
        return username;
    }

    public List<String> milkSummary() {
        int sum = cocktailService.getAllCocktails().stream()
                .filter(cocktail -> cocktail.getName().contains("Milk"))
                .map(cocktail -> cocktailService.getCocktailDetails(cocktail.getId()))
                .flatMap(details -> details.getInstructions().stream())
                .filter(instruction -> instruction.getIngredient().getName().equals("Milch"))
                .mapToInt(Instruction::getAmountCL)
                .sum();
        return List.of(String.valueOf(sum));
    }

    static final Map<String, Set<Long>> allFridges = new HashMap<>();

}
