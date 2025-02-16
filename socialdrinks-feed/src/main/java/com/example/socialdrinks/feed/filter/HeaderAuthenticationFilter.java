package com.example.socialdrinks.feed.filter;

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = request.getHeader("X-User");
        String rolesHeader = request.getHeader("X-Roles");

        LOGGER.info("User '{}' with roles {}", username, rolesHeader);

        if (username != null && !username.isEmpty() && rolesHeader != null && !rolesHeader.isEmpty()) {
            var authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(rolesHeader);

            // Setze den Username in den SecurityContext
            AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);

            // Setze den Authentication-Token in den SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

}
