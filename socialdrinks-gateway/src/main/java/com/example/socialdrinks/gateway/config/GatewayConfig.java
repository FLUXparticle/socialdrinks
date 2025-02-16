package com.example.socialdrinks.gateway.config;

import org.springframework.cloud.gateway.route.*;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.context.annotation.*;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("cocktail-app", r -> r
                        .path("/bars/**")
                        .uri("http://localhost:8081"))

                // Route für statische Angular-Dateien
                .route("drinks-resources", r -> r
                        .path("/drinks/")
                        .uri("forward:/drinks/index.html"))

                .route("social-resources", r -> r
                        .path("/social/")
                        .uri("forward:/social/index.html"))

                // Cocktail-REST-API
                .route("cocktail-service", r -> r
                        .path("/api/cocktails/**", "/api/ingredients/**")
                        .uri("http://localhost:8082"))

                .route("auth-service", r -> r
                        .path("/auth/**") // Alle Anfragen an /auth/** gehen zum Auth-Service
                        .uri("http://localhost:8083"))

                // Fridge-REST-API
                .route("fridge-service", r -> r
                        .path("/api/fridge/**")
                        .uri("http://localhost:8084"))

                .route("feed-service", r -> r
                        .path("/api/feed/**")
                        .uri("http://localhost:8085"))

                .route("chat-service", r -> r
                        .path("/chat/**")
                        .uri("ws://localhost:8086"))
                
                .build();
    }

}
