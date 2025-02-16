package com.example.socialdrinks.fridge.config;

import org.springframework.context.annotation.*;
import org.springframework.web.reactive.function.client.*;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8082") // URL des cocktail-service
                .build();
    }

}
