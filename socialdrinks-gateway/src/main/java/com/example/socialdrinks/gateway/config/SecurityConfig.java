package com.example.socialdrinks.gateway.config;

import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.security.config.*;
import org.springframework.security.config.web.server.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.security.web.server.*;
import reactor.core.publisher.*;

import java.util.*;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, ReactiveJwtDecoder jwtDecoder) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                        .bearerTokenConverter(exchange -> {
                            List<HttpCookie> jwtCookies = exchange.getRequest().getCookies().get("jwt");

                            if (jwtCookies == null || jwtCookies.isEmpty()) {
                                return Mono.empty();
                            }

                            String token = jwtCookies.get(0).getValue();

                            return Mono.just(new BearerTokenAuthenticationToken(token));
                        })
                )
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/**").authenticated()  // API benötigen Authentifizierung
                        .anyExchange().permitAll()  // Alle anderen Anfragen werden zugelassen
                )
                .build();
    }

}
