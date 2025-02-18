package com.example.socialdrinks.cocktails.repository;

import com.example.socialdrinks.model.entity.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class CocktailRepositoryTest {

    @Autowired
    private CocktailRepository cocktailRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    void testFindByNameContains() {
        // Testdaten anlegen
        Ingredient ingredient = new Ingredient(1L, "Minze");
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        Instruction instruction = new Instruction(null, savedIngredient);
        Cocktail cocktail = new Cocktail("Mojito", List.of(instruction));
        cocktailRepository.save(cocktail);

        Collection<Cocktail> found = cocktailRepository.findByNameContains("Mojito");
        assertThat(found).isNotEmpty();
    }

}
