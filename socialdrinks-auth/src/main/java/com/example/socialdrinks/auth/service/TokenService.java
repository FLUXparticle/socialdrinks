package com.example.socialdrinks.auth.service;

import org.springframework.security.oauth2.jose.jws.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

@Service
public class TokenService {

    private final UserService userService;

    private final JwtEncoder jwtEncoder;

    public TokenService(UserService userService, JwtEncoder jwtEncoder) {
        this.userService = userService;
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(String username, String password) {
        String role = userService.getRole(username, password);
        if (role == null) {
            throw new RuntimeException("Invalid credentials");
        }

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(username)
                .claim("scope", List.of(role))  // User-Rolle als Claim
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600)) // Token für 1 Stunde gültig
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

}
