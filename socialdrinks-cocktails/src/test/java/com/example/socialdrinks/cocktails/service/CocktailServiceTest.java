package com.example.socialdrinks.cocktails.service;

import com.example.socialdrinks.cocktails.repository.*;
import com.example.socialdrinks.model.entity.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CocktailServiceTest {

    @Mock
    private CocktailRepository cocktailRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private CocktailService cocktailService;

    @Test
    void testSearch() {
        String query = "Mojito";

        Ingredient ingredient = new Ingredient(1L, "Minze");
        Cocktail cocktail = new Cocktail("Mojito", List.of(new Instruction(null, ingredient)));

        when(cocktailRepository.findByNameContains(query)).thenReturn(List.of(cocktail));
        when(ingredientRepository.findByNameContains(query)).thenReturn(List.of(ingredient));

        Collection<Cocktail> result = cocktailService.search(query);
        assertThat(result).isNotEmpty();
    }

}
