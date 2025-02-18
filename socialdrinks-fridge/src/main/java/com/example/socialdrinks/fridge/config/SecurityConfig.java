package com.example.socialdrinks.fridge.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.web.server.*;
import org.springframework.security.web.server.*;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .anyExchange().permitAll()
                )
                .build();
    }

}
