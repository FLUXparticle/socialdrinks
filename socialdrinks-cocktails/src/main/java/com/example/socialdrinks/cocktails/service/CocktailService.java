package com.example.socialdrinks.cocktails.service;

import com.example.socialdrinks.cocktails.repository.*;
import com.example.socialdrinks.model.entity.*;
import org.slf4j.*;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.stereotype.*;

import java.util.*;

import static java.util.Collections.*;

@Service
public class CocktailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CocktailService.class);

    private final CocktailRepository cocktailRepository;

    private final IngredientRepository ingredientRepository;

    private final RabbitTemplate rabbitTemplate;

    public CocktailService(CocktailRepository cocktailRepository, IngredientRepository ingredientRepository, RabbitTemplate rabbitTemplate) {
        this.cocktailRepository = cocktailRepository;
        this.ingredientRepository = ingredientRepository;
        this.rabbitTemplate = rabbitTemplate;
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
        Collection<Cocktail> maybePossibleCocktails = cocktailRepository.findDistinctByInstructionsIngredientIdIn(ingredientIDs);

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

    /**
     * Fragt über RabbitMQ beim FeedService den Durchschnitt der Bewertungen für den angegebenen Cocktail ab.
     * Falls keine Antwort oder ein Fehler auftritt, wird null zurückgegeben.
     */
    public Double getAverageRating(Long cocktailId) {
        try {
            // Sende die Anfrage an den FeedService.
            // Hier wird angenommen, dass der Exchange "rating.exchange" und das Routing Key "rating.request" verwendet werden.
            Object response = rabbitTemplate.convertSendAndReceive("rating.exchange", "rating.request", cocktailId);
            if (response instanceof Double value) {
                return value;
            }
        } catch(Exception e) {
            LOGGER.error("Fehler beim Abfragen der Durchschnittsbewertung: {}", e.getMessage());
        }
        return null;
    }

}
