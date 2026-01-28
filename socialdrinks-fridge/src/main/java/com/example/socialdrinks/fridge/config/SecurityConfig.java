package com.example.socialdrinks.fridge.config;

import com.example.socialdrinks.fridge.filter.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;

@Configuration
public class SecurityConfig {

    private final HeaderAuthenticationFilter headerAuthenticationFilter;

    public SecurityConfig(HeaderAuthenticationFilter headerAuthenticationFilter) {
        this.headerAuthenticationFilter = headerAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(headerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .build();
    }

}
