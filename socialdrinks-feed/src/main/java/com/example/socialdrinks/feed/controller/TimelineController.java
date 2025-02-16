package com.example.socialdrinks.feed.controller;

import com.example.socialdrinks.feed.entity.*;
import com.example.socialdrinks.feed.service.*;
import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.*;

import java.util.*;

@RestController
@RequestMapping("/api/feed")
public class TimelineController {

    private final TimelineService timelineService;

    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }

    @ResponseBody
    @GetMapping("/username")
    public Map<String, Object> username(Authentication authentication) {
        String username = authentication.getName();
        return Map.of("username", username);
    }

    @GetMapping(value = "/posts", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Post> streamTimeline(Authentication authentication) {
        String subscriberName = authentication.getName();
        return timelineService.getTimelinePostUpdates(subscriberName);
    }

    /**
     * Erzeugt einen neuen Post mit Cocktail-ID, Cocktail-Name und Rating.
     * Erwartete JSON-Payload:
     * {
     *    "cocktailId": 123,
     *    "cocktailName": "Mojito",
     *    "rating": 5
     * }
     */
    @PostMapping("/posts")
    public Map<String, Object> createPost(Authentication authentication, @RequestBody Map<String, Object> payload) {
        Long cocktailId = Long.valueOf(payload.get("cocktailId").toString());
        String cocktailName = (String) payload.get("cocktailName");
        Integer rating = Integer.valueOf(payload.get("rating").toString());

        String creatorName = authentication.getName();

        // Hier wird die eigentliche Logik aufgerufen.
        timelineService.createPost(creatorName, cocktailId, cocktailName, rating);

        return Map.of("success", "ok");
    }

    /**
     * Sucht in den Timeline-Posts über den Suchbegriff.
     * Hier wird intern ein Flux verwendet, aber wir sammeln alle
     * gefundenen Posts und geben sie als ganzes JSON-Array zurück.
     * URL: GET /api/feed/search?q=Suchbegriff
     */
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> searchPosts(@RequestParam("q") String query) {
        try {
            List<Post> posts = timelineService.searchPosts(query);

            return Map.of("searchResults", posts);
        } catch (Exception e) {
            // Im Fehlerfall wird ein HTTP 200 zurückgegeben, aber mit einer Fehlermeldung.
            return Map.of("error", e.getMessage());
        }
    }

}
