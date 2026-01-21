package com.example.socialdrinks.fridge.service;

import com.example.socialdrinks.fridge.entity.*;
import com.example.socialdrinks.fridge.model.*;
import com.example.socialdrinks.fridge.repository.*;
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
    private final FridgeItemRepository fridgeItemRepository;

    @Autowired
    public FridgeService(CocktailRemoteService cocktailService, FridgeItemRepository fridgeItemRepository) {
        this.cocktailService = cocktailService;
        this.fridgeItemRepository = fridgeItemRepository;
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
        String username = currentUsername();
        if (inFridge) {
            fridgeItemRepository.findByUsernameAndIngredientId(username, ingredientId)
                    .orElseGet(() -> fridgeItemRepository.save(new FridgeItem(null, username, ingredientId)));
        } else {
            fridgeItemRepository.deleteByUsernameAndIngredientId(username, ingredientId);
        }
    }

    // Methode zum Abrufen möglicher Cocktails basierend auf den Zutaten im Kühlschrank
    public List<Cocktail> getPossibleCocktails() {
        Set<Long> fridge = getFridge();
        return fridge.isEmpty()
                ? List.of()
                : cocktailService.getPossibleCocktails(fridge);
    }

    private Set<Long> getFridge() {
        String username = currentUsername();
        return fridgeItemRepository.findByUsername(username).stream()
                .map(FridgeItem::getIngredientId)
                .collect(Collectors.toSet());
    }

    private String currentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication == null ? "anonymous" : authentication.getName();
        LOGGER.info("User '{}'", username);
        return username;
    }

}
