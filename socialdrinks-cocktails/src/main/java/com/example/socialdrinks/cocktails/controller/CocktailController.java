package com.example.socialdrinks.cocktails.controller;

import com.example.socialdrinks.cocktails.service.*;
import com.example.socialdrinks.model.entity.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class CocktailController {

    private final CocktailService cocktailService;

    public CocktailController(CocktailService cocktailService) {
        this.cocktailService = cocktailService;
    }

    @GetMapping("/")
    public String cocktails() {
        return "index";
    }

    @GetMapping("/search")
    public String search() {
        return "search";
    }

    @PostMapping("/search")
    public String search(@RequestParam String query, Model model) {
        Collection<Cocktail> cocktails = cocktailService.search(query);
        model.addAttribute("cocktails", cocktails);
        return "result";
    }

    @GetMapping("/cocktails")
    public String cocktails(Model model) {
        Collection<Cocktail> cocktails = cocktailService.getAllCocktails();
        model.addAttribute("cocktails", cocktails);
        return "cocktails";
    }

    @GetMapping("/cocktails/{id}")
    public String cocktail(@PathVariable Long id, Model model) {
        Cocktail cocktail = cocktailService.getCocktailWithID(id);
        model.addAttribute("name", cocktail.getName());
        model.addAttribute("instructions", cocktail.getInstructions());
        return "cocktail";
    }

    @GetMapping("/ingredients")
    public String ingredients(Model model) {
        Collection<Ingredient> ingredients = cocktailService.getAllIngredients();
        model.addAttribute("ingredients", ingredients);
        return "ingredients";
    }

    @GetMapping("/ingredients/{id}")
    public String ingredient(@PathVariable Long id, Model model) {
        Ingredient ingredient = cocktailService.getIngredientWithID(id);
        Collection<Cocktail> cocktails = cocktailService.getAllCocktailsWithIngredient(id);
        model.addAttribute("name", ingredient.getName());
        model.addAttribute("cocktails", cocktails);
        return "ingredient";
    }

}
