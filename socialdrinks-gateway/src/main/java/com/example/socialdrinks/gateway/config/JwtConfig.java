package com.example.socialdrinks.gateway.config;

import org.springframework.context.annotation.*;
import org.springframework.security.oauth2.jose.jws.*;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.*;
import javax.crypto.spec.*;

@Configuration
public class JwtConfig {

    private static final String SECRET = "s/4KMb61LOrMYYAn4rfaQYSgr+le5SMrsMzKw8G6bXc=";

    private final SecretKey secretKey;

    public JwtConfig() {
        secretKey = new SecretKeySpec(SECRET.getBytes(), "HmacSHA256");
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

}
