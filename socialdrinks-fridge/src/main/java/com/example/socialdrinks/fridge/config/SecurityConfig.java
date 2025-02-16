package com.example.socialdrinks.fridge.config;

import com.example.socialdrinks.fridge.filter.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.web.server.*;
import org.springframework.security.web.server.*;

@Configuration
public class SecurityConfig {

    private final HeaderAuthenticationFilter headerAuthenticationFilter;

    public SecurityConfig(HeaderAuthenticationFilter headerAuthenticationFilter) {
        this.headerAuthenticationFilter = headerAuthenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                // Füge den HeaderAuthenticationFilter an einer passenden Stelle in der Filterkette ein.
                .addFilterBefore(headerAuthenticationFilter, SecurityWebFiltersOrder.AUTHORIZATION)
                .authorizeExchange(exchange -> exchange
                        .anyExchange().authenticated()
                )
                .build();
    }

}
