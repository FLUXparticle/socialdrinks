package com.example.socialdrinks.gateway.filter;

import org.slf4j.*;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.http.server.reactive.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.oauth2.server.resource.authentication.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;
import reactor.core.publisher.*;

import java.util.*;

import static java.util.stream.Collectors.*;

@Component
public class AuthToHeaderFilter implements GlobalFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthToHeaderFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        LOGGER.info("Request to: {}", exchange.getRequest().getURI());

        // Greife auf den SecurityContext zu
        Mono<Void> monoAuth = ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    Authentication auth = securityContext.getAuthentication();
                    LOGGER.info("auth: {} isAuthenticated: {}", auth.getClass().getName(), auth.isAuthenticated());

                    if (auth instanceof JwtAuthenticationToken jwtAuth) {
                        String username = jwtAuth.getName();
                        List<String> roles = jwtAuth.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(toList());
                        String rolesStr = String.join(",", roles);

                        LOGGER.info("User '{}' with roles {} found - adding headers.", username, rolesStr);

                        // Erstelle einen neuen Request mit den zusätzlichen Headern
                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-User", username)
                                .header("X-Roles", rolesStr)
                                .build();

                        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

                        // Wichtig: SecurityContext explizit in den Reactor Context übernehmen!
                        return chain.filter(mutatedExchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                    }

                    return Mono.empty();
                });

        Mono<Void> monoNoAuth = Mono.defer(() -> chain.filter(exchange));

        return monoAuth.switchIfEmpty(monoNoAuth);
    }

}
