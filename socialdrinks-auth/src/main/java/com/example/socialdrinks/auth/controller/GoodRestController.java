package com.example.socialdrinks.auth.controller;

import com.example.socialdrinks.auth.service.GoodService;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/auth/good", produces = MediaType.APPLICATION_JSON_VALUE)
public class GoodRestController {

    private final GoodService goodService;

    public GoodRestController(GoodService goodService) {
        this.goodService = goodService;
    }

    @Secured({"SCOPE_USER", "SCOPE_ADMIN"})
    @GetMapping("/me")
    public Map<String, String> getMyText(JwtAuthenticationToken authentication) {
        String username = authentication.getName();
        return Map.of(
                "username", username,
                "text", goodService.getText(username)
        );
    }

    @Secured({"SCOPE_USER", "SCOPE_ADMIN"})
    @PostMapping(value = "/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> saveMyTextJson(JwtAuthenticationToken authentication,
                                              @RequestBody Map<String, String> body) {
        return save(authentication, body.get("text"));
    }

    @Secured({"SCOPE_USER", "SCOPE_ADMIN"})
    @PostMapping(value = "/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Map<String, String> saveMyTextForm(JwtAuthenticationToken authentication,
                                              @RequestParam(name = "text", required = false) String text) {
        return save(authentication, text);
    }

    @Secured("SCOPE_ADMIN")
    @GetMapping("/all")
    public Map<String, String> getAllTexts() {
        return goodService.getAllTexts();
    }

    private Map<String, String> save(JwtAuthenticationToken authentication, String text) {
        String username = authentication.getName();
        goodService.saveText(username, text);
        return Map.of(
                "username", username,
                "text", goodService.getText(username)
        );
    }
}
