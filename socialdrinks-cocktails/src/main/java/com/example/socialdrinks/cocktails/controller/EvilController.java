package com.example.socialdrinks.cocktails.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/evil", produces = MediaType.APPLICATION_JSON_VALUE)
public class EvilController {

    private final List<Map<String, String>> capturedTokens = Collections.synchronizedList(new ArrayList<>());

    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of(
                "app", "socialdrinks-evil",
                "message", "Ready to host attack demos",
                "status", "up",
                "captures", capturedTokens.size()
        );
    }

    @CrossOrigin(origins = { "http://localhost:8083", "http://localhost:8080" }, methods = RequestMethod.POST, allowedHeaders = "*")
    @PostMapping(value = "/collect", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> collectToken(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String token = body.getOrDefault("token", "");
        String page = body.getOrDefault("page", "");
        String notes = body.getOrDefault("notes", "");

        Map<String, String> entry = new HashMap<>();
        entry.put("token", token);
        entry.put("page", page);
        entry.put("notes", notes);
        entry.put("origin", request.getHeader("Origin"));
        entry.put("timestamp", Instant.now().toString());

        capturedTokens.add(entry);

        return Map.of(
                "status", "recorded",
                "entries", capturedTokens.size()
        );
    }

    @GetMapping("/loot")
    public List<Map<String, String>> loot() {
        return List.copyOf(capturedTokens);
    }
}
