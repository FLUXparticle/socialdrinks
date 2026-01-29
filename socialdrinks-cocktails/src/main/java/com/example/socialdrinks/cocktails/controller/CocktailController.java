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
    private final CartService cartService;

    public CocktailController(CocktailService cocktailService, CartService cartService) {
        this.cocktailService = cocktailService;
        this.cartService = cartService;
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

    @GetMapping("/cart")
    public String cart(Model model) {
        model.addAttribute("ingredients", cartService.getIngredients());
        model.addAttribute("cocktails", cartService.getCocktailsForSelection());
        return "cart";
    }

    @PostMapping("/cart/actions/add-cocktail")
    public String addCocktailToCart(@RequestParam Long cocktailId) {
        cartService.addCocktail(cocktailId);
        return "redirect:/cart";
    }

    @PostMapping("/cart/items/{ingredientId}/remove")
    public String removeIngredientFromCart(@PathVariable Long ingredientId) {
        cartService.removeIngredient(ingredientId);
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart() {
        cartService.clear();
        return "redirect:/cart";
    }

}
