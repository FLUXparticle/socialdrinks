package com.example.socialdrinks.fridge.service;

import com.example.socialdrinks.fridge.entity.*;
import com.example.socialdrinks.fridge.model.*;
import com.example.socialdrinks.fridge.repository.*;
import com.example.socialdrinks.model.entity.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FridgeServiceTest {

    @Mock
    private CocktailRemoteService cocktailService;

    @Mock
    private FridgeItemRepository fridgeItemRepository;

    @InjectMocks
    private FridgeService fridgeService;

    @Test
    void testGetFridgeIngredients() {
        // Simulierte Zutaten
        Ingredient ingredient1 = new Ingredient(1L, "Rum");
        Ingredient ingredient2 = new Ingredient(2L, "Minze");

        when(fridgeItemRepository.findByUsername("anonymous")).thenReturn(List.of());
        when(cocktailService.getAllIngredients()).thenReturn(List.of(ingredient1, ingredient2));

        // Zutaten abrufen
        List<FridgeIngredient> ingredients = fridgeService.getFridgeIngredients();
        Assertions.assertEquals(2, ingredients.size());
        Assertions.assertEquals(1L, ingredients.get(0).getId());
        Assertions.assertEquals("Rum", ingredients.get(0).getName());
        Assertions.assertFalse(ingredients.get(0).isInFridge());
        Assertions.assertEquals(2L, ingredients.get(1).getId());
        Assertions.assertEquals("Minze", ingredients.get(1).getName());
        Assertions.assertFalse(ingredients.get(1).isInFridge());
    }

    @Test
    void testGetPossibleCocktails() {
        when(fridgeItemRepository.findByUsername("anonymous"))
                .thenReturn(List.of(
                        new FridgeItem(1L, "anonymous", 1L),
                        new FridgeItem(2L, "anonymous", 2L)
                ));

        // Simulierte mögliche Cocktails
        Cocktail cocktail = new Cocktail("Mojito", List.of());

        when(cocktailService.getPossibleCocktails(any())).thenReturn(List.of(cocktail));

        // Abrufen der möglichen Cocktails
        List<Cocktail> cocktails = fridgeService.getPossibleCocktails();
        Assertions.assertEquals(1, cocktails.size());
        Assertions.assertEquals(cocktail, cocktails.get(0));
    }

}
