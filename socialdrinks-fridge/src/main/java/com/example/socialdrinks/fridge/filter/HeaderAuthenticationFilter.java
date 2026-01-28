package com.example.socialdrinks.fridge.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

import java.io.*;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String username = request.getHeader("X-User");
        String rolesHeader = request.getHeader("X-Roles");

        if (username != null && !username.isEmpty() && rolesHeader != null && !rolesHeader.isEmpty()) {
            var authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(rolesHeader);
            AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            LOGGER.info("User '{}' with roles {} authenticated: {}", username, rolesHeader, authToken.isAuthenticated());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            LOGGER.info("No authentication header found");
        }

        filterChain.doFilter(request, response);
    }
}
