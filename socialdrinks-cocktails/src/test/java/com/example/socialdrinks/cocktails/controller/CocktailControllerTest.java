package com.example.socialdrinks.cocktails.controller;

import com.example.socialdrinks.cocktails.service.*;
import com.example.socialdrinks.model.entity.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.test.context.bean.override.mockito.*;
import org.springframework.test.web.servlet.*;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CocktailController.class)
class CocktailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CocktailService cocktailService;

    @Test
    void testGetCocktailsPage() throws Exception {
        // Beispielhafte Daten vorbereiten
        List<Cocktail> cocktails = List.of(new Cocktail("Mojito", List.of()));
        when(cocktailService.getAllCocktails()).thenReturn(cocktails);

        mockMvc.perform(get("/cocktails"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("cocktails"))
               .andExpect(view().name("cocktails"));
    }

    @Test
    void testPostSearch() throws Exception {
        String query = "Mojito";
        List<Cocktail> foundCocktails = List.of(new Cocktail("Mojito", List.of()));
        when(cocktailService.search(query)).thenReturn(foundCocktails);

        mockMvc.perform(post("/search").param("query", query))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("cocktails"))
               .andExpect(view().name("result"));
    }

}
