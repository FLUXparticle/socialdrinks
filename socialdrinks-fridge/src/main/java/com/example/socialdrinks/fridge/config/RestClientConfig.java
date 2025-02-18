package com.example.socialdrinks.fridge.config;

import org.springframework.context.annotation.*;
import org.springframework.web.client.*;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:8082")
                .build();
    }
}
