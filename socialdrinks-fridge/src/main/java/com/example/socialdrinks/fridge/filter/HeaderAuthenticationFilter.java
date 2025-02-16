package com.example.socialdrinks.fridge.filter;

import org.slf4j.*;
import org.springframework.http.server.reactive.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;
import reactor.core.publisher.*;

@Component
public class HeaderAuthenticationFilter implements WebFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderAuthenticationFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String username = request.getHeaders().getFirst("X-User");
        String rolesHeader = request.getHeaders().getFirst("X-Roles");

        if (username != null && !username.isEmpty() && rolesHeader != null && !rolesHeader.isEmpty()) {
            var authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(rolesHeader);

            AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);

            LOGGER.info("User '{}' with roles {} authenticated: {}", username, rolesHeader, authToken.isAuthenticated());

            // Übertrage den Authentication in den Reactor-SecurityContext
            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authToken));
        } else {
            LOGGER.info("No authentication header found");
            return chain.filter(exchange);
        }
    }

}
