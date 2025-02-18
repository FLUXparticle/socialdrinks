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
        // TODO: Schreibe einen Test, der einen Cocktail anhand der ID abruft.
        // Verwende die Methode restTemplate.getForEntity().
        // Stelle sicher, dass die Antwort OK ist
        // und die Map die Keys 'name' und 'instructions' enthält.
    }

    @Test
    void testSearchCocktails() {
        // TODO: Schreibe einen Test für GET /cocktails/search.
        // Verwende einen Suchbegriff wie 'Milch'.
        // Stelle sicher, dass die Antwort OK ist
        // und mindestens ein Cocktail zurückgegeben wird.
        // Bonus: Einer der Cocktails ist der "Pink Power"
    }

    @Test
    void testPostPossibleRecipes() {
        // TODO: Schreibe einen Test für POST /possible.
        // Sende eine JSON-Payload mit den Ingredient-IDs 7 und 30.
        // Stelle sicher, dass genau ein möglicher Cocktail zurückgelifert wird.
        // Bonus: Dieser Cocktails ist der "Pink Power"
    }


    @Test
    void testGetAllIngredients() {
        // TODO: Schreibe einen Test für GET /ingredients.
        // Stelle sicher, dass die Antwort OK ist
        // und mindestens eine Zutat zurückgegeben wird.
    }

    @Test
    void testGetIngredientById() {
        // TODO: Schreibe einen Test für GET /ingredients/{id}.
        // Stelle sicher, dass die Antwort OK ist
        // und eine gültige Zutat zurückkommt.
    }

    @Test
    void testGetCocktailsByIngredient() {
        // TODO: Schreibe einen Test für GET /ingredients/{id}/cocktails.
        // Stelle sicher, dass die Antwort OK ist
        // und eine Liste von Cocktails zurückkommt.
    }
}