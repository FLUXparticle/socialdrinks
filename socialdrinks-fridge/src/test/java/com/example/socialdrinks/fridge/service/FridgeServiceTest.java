package com.example.socialdrinks.fridge.service;

import com.example.socialdrinks.fridge.model.*;
import com.example.socialdrinks.model.entity.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import reactor.core.publisher.*;
import reactor.test.*;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FridgeServiceTest {

    @Mock
    private CocktailRemoteService cocktailService;

    @InjectMocks
    private FridgeService fridgeService;

    @BeforeEach
    void setUp() {
        // Leeren des "Kühlschrank"-Speichers vor jedem Test
        FridgeService.allFridges.clear();
    }

    @Test
    void testGetFridgeIngredients() {
        // Simulierte Zutaten
        Ingredient ingredient1 = new Ingredient(1L, "Rum");
        Ingredient ingredient2 = new Ingredient(2L, "Minze");

        when(cocktailService.getAllIngredients()).thenReturn(Flux.just(ingredient1, ingredient2));

        // Zutaten abrufen
        StepVerifier.create(fridgeService.getFridgeIngredients())
                .expectNext(new FridgeIngredient(1L, "Rum", false))
                .expectNext(new FridgeIngredient(2L, "Minze", false))
                .verifyComplete();
    }

    @Test
    void testGetPossibleCocktails() {
        // Zutaten in den "Kühlschrank" legen
        fridgeService.updateIngredientStatus(1L, true).subscribe();
        fridgeService.updateIngredientStatus(2L, true).subscribe();

        // Simulierte mögliche Cocktails
        Cocktail cocktail = new Cocktail("Mojito", List.of());

        when(cocktailService.getPossibleCocktails(any())).thenReturn(Flux.just(cocktail));

        // Abrufen der möglichen Cocktails
        StepVerifier.create(fridgeService.getPossibleCocktails())
                .expectNext(cocktail)
                .verifyComplete();
    }

}
