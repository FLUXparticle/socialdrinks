package com.example.socialdrinks.cocktails.service;

import com.example.socialdrinks.cocktails.repository.*;
import com.example.socialdrinks.model.entity.*;
import org.slf4j.*;
import org.springframework.stereotype.*;

import java.util.*;

import static java.util.Collections.*;

@Service
public class CocktailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CocktailService.class);

    private final CocktailRepository cocktailRepository;

    private final IngredientRepository ingredientRepository;

    public CocktailService(CocktailRepository cocktailRepository, IngredientRepository ingredientRepository) {
        this.cocktailRepository = cocktailRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public Collection<Cocktail> getAllCocktails() {
        return cocktailRepository.findAll();
    }

    public Collection<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Collection<Instruction> getInstructions(long cocktailID) {
        return cocktailRepository.findById(cocktailID)
                .map(Cocktail::getInstructions)
                .orElse(emptyList());
    }

    public Collection<Cocktail> getAllCocktailsWithIngredient(Long ingredientsId) {
        Collection<Cocktail> cocktails = getAllCocktailsWithIngredients(singleton(ingredientsId));
        return new TreeSet<>(cocktails);
    }

    public Cocktail getCocktailWithID(Long id) {
        return cocktailRepository.findById(id).orElse(null);
    }

    public Ingredient getIngredientWithID(Long id) {
        return ingredientRepository.findById(id).orElse(null);
    }

    public Collection<Cocktail> search(String query) {
        Collection<Cocktail> cocktailsWithName = cocktailRepository.findByNameContains(query);
        Collection<Ingredient> ingredientsWithName = ingredientRepository.findByNameContains(query);

        Set<Long> ingredientIDs = new HashSet<>();
        for (Ingredient ingredient : ingredientsWithName) {
            ingredientIDs.add(ingredient.getId());
        }

        Collection<Cocktail> cocktailsWithIngredients = getAllCocktailsWithIngredients(ingredientIDs);

        SortedSet<Cocktail> result = new TreeSet<>();
        result.addAll(cocktailsWithName);
        result.addAll(cocktailsWithIngredients);

        return result;
    }

    public List<Cocktail> getPossibleCocktails(Set<Long> ingredientIDs) {
        if (ingredientIDs == null || ingredientIDs.isEmpty()) {
            return emptyList();
        }

        // Alle Cocktails abrufen
        Collection<Cocktail> maybePossibleCocktails = getAllCocktailsWithIngredients(ingredientIDs);

        // Filtern der Cocktails, deren alle Zutaten in ingredientIDs enthalten sind
        return maybePossibleCocktails.stream()
                .filter(cocktail -> cocktail.getInstructions().stream()
                        .allMatch(instruction -> {
                            Long ingredientID = instruction.getIngredient().getId();
                            return ingredientIDs.contains(ingredientID);
                        })
                )
                .toList();
    }

    private Collection<Cocktail> getAllCocktailsWithIngredients(Set<Long> ingredientIDs) {
        return cocktailRepository.findDistinctByInstructionsIngredientIdIn(ingredientIDs);
    }

}
