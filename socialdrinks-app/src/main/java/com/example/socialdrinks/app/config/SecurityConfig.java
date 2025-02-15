package com.example.socialdrinks.app.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.web.*;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .build();
    }

}
