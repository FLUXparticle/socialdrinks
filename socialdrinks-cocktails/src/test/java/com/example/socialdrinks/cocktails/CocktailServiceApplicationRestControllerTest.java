package com.example.socialdrinks.cocktails;

import com.example.socialdrinks.model.entity.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.web.client.*;
import org.springframework.boot.test.web.server.*;
import org.springframework.http.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CocktailServiceApplicationRestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api";
    }

    @Test
    void testGetAllCocktails() {
        ResponseEntity<Cocktail[]> response = restTemplate.getForEntity(baseUrl + "/cocktails", Cocktail[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isEqualTo(69);
    }

    @Test
    void testGetCocktailById() {
        long cocktailId = 1;  // Dies sollte ein gültiger ID-Wert sein
        ResponseEntity<Map> response = restTemplate.getForEntity(baseUrl + "/cocktails/" + cocktailId, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKeys("name", "instructions");
    }

    @Test
    void testSearchCocktails() {
        String query = "Milch";
        ResponseEntity<Cocktail[]> response = restTemplate.getForEntity(baseUrl + "/cocktails/search?query=" + query, Cocktail[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testPostPossibleRecipes() {
        Map<String, List<Long>> requestPayload = new HashMap<>();
        requestPayload.put("ingredientIDs", Arrays.asList(7L, 30L));

        ResponseEntity<Cocktail[]> response = restTemplate.postForEntity(
                baseUrl + "/possible",
                requestPayload,
                Cocktail[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testGetAllIngredients() {
        ResponseEntity<Ingredient[]> response = restTemplate.getForEntity(baseUrl + "/ingredients", Ingredient[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testGetIngredientById() {
        long ingredientId = 1;
        ResponseEntity<Ingredient> response = restTemplate.getForEntity(baseUrl + "/ingredients/" + ingredientId, Ingredient.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testGetCocktailsByIngredient() {
        long ingredientId = 1;
        ResponseEntity<Map> response = restTemplate.getForEntity(baseUrl + "/ingredients/" + ingredientId + "/cocktails", Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsKey("cocktails");
    }
}