package com.example.socialdrinks.auth.controller;

import com.example.socialdrinks.auth.service.*;
import org.springframework.http.*;
import org.springframework.security.access.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.TEXT_PLAIN_VALUE)
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        String token = tokenService.generateToken(username, password);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
//                .httpOnly(false)
//                .secure(false)  // ACHTUNG: In Produktion sollte hier true stehen (nur HTTPS)
//                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(Map.of("token", token));
    }

    @GetMapping("/public")
    public String getPublic() {
        return "Public endpoint - no authentication required.";
    }

    @GetMapping("/user")
    @Secured("SCOPE_USER")
    public String getUser(JwtAuthenticationToken authentication) {
        return "Hello, " + authentication.getName() + " " + authentication.getAuthorities();
    }

    @GetMapping("/admin")
    @Secured("SCOPE_ADMIN")
    public String getAdmin(Authentication authentication) {
        return "Admin access granted for " + authentication.getName() + " " + authentication.getAuthorities();
    }

}
