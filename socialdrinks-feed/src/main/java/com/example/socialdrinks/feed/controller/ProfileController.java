package com.example.socialdrinks.feed.controller;

import com.example.socialdrinks.feed.entity.*;
import com.example.socialdrinks.feed.model.*;
import com.example.socialdrinks.feed.service.*;
import org.springframework.context.support.*;
import org.springframework.security.core.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.stream.Collectors.*;

@RestController
@RequestMapping("/api/feed")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Liefert den Profile-Status des angemeldeten Users, insbesondere die Anzahl der Abonnenten.
     * Aufruf: GET /api/feed/profile
     */
    @GetMapping("/profile")
    public Map<String, Object> profile(Authentication authentication) {
        String username = authentication.getName();

        User user = profileService.getUser(username, false);
        long subscriberCount = profileService.getSubscriberCount(user.getId());

        return Map.of("subscriberCount", subscriberCount);
    }

    /**
     * Abonniert einen anderen User.
     * Erwartete JSON-Payload:
     * {
     *   "creatorName": "username-des-creators"
     * }
     * Aufruf: POST /api/feed/subscribe
     * Bei Erfolg wird { "success": "..." } zurückgegeben, bei Fehlern { "error": "..." }.
     */
    @PostMapping("/subscribe")
    public Map<String, String> subscribe(
            Authentication authentication,
            @Validated @RequestBody SubscribeForm subscribeForm,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(joining("\n"));
            return Map.of("error", errors);
        }

        String subscriberName = authentication.getName();
        String creatorName = subscribeForm.getCreatorName();

        String error = profileService.subscribe(null, creatorName, subscriberName);
        if (!error.isEmpty()) {
            return Map.of("error", error);
        }

        return Map.of("success", String.format("User '%s' erfolgreich abonniert.", creatorName));
    }

}
