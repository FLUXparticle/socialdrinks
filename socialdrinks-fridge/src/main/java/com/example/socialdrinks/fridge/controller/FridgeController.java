package com.example.socialdrinks.fridge.controller;

import com.example.socialdrinks.fridge.model.*;
import com.example.socialdrinks.fridge.service.*;
import com.example.socialdrinks.model.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.servlet.mvc.method.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.Map.*;

@RestController
@RequestMapping("/api/fridge")
public class FridgeController {

    private final FridgeService fridgeService;

    @Autowired
    public FridgeController(FridgeService fridgeService) {
        this.fridgeService = fridgeService;
    }

    @GetMapping("/headers")
    public Set<Entry<String, List<String>>> getHeaders(@RequestHeader HttpHeaders headers) {
        return headers.entrySet();
    }

    // Endpoint zum Abrufen aller Zutaten im Kühlschrank
    @GetMapping("/ingredients")
    public List<FridgeIngredient> getFridgeIngredients() {
        return fridgeService.getFridgeIngredients();
    }

    // Endpoint zum Aktualisieren des Status einer Zutat im Kühlschrank
    @PatchMapping("/ingredients/{id}")
    public void updateIngredientStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> payload) {
        boolean inFridge = payload.getOrDefault("inFridge", false);
        fridgeService.updateIngredientStatus(id, inFridge);
    }

    // Endpoint zum Abrufen möglicher Cocktails
    @GetMapping("/possible")
    public List<Cocktail> getPossibleCocktails() {
        return fridgeService.getPossibleCocktails();
    }

    // Endpoint zum Mischen eines Cocktails
    @GetMapping(value = "/mix/{cocktailId}",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter mixCocktail(@PathVariable Long cocktailId) {
        SseEmitter emitter = new SseEmitter();
        List<String> steps = fridgeService.mix(cocktailId);
        new Thread(() -> {
            try {
                for (String step : steps) {
                    emitter.send(step);
                    Thread.sleep(1000);
                }
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        }).start();
        return emitter;
    }

    @GetMapping(value = "/milk/summary",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter milkSummary() {
        SseEmitter emitter = new SseEmitter();
        List<String> steps = fridgeService.milkSummary();
        new Thread(() -> {
            try {
                for (String step : steps) {
                    emitter.send(step);
                    Thread.sleep(1000);
                }
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        }).start();
        return emitter;
    }


}
