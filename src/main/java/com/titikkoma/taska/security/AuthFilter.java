package com.titikkoma.taska.security;

import com.titikkoma.taska.base.error.UnauthorizeError;
import com.titikkoma.taska.entity.CustomAuthPrincipal;
import com.titikkoma.taska.model.User;
import com.titikkoma.taska.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthFilter extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;

    // 2. Inject HandlerExceptionResolver melalui constructor (best practice)
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public AuthFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (request.getRequestURI().startsWith("/v1/auth")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = request.getHeader("Authorization");
            if (token == null || token.isEmpty()) {
                throw new UnauthorizeError("Required Authorization header is missing");
            }

            Map<String, Object> cond = new HashMap<>();
            cond.put("token", token);
            User user = userRepository.findOne(cond)
                    .orElseThrow(() -> new UnauthorizeError("User not found"));

            if (!user.getToken().equals(token)) {
                throw new UnauthorizeError("Token does not match");
            }

            Instant now = Instant.now();
            if (user.getExpired_at().toInstant().toEpochMilli() < now.toEpochMilli()) {
                throw new UnauthorizeError("Token expired");
            }

            CustomAuthPrincipal principal = new CustomAuthPrincipal(
                    user.getId(),
                    user.getRole(),
                    user.getName(),
                    user.getOrganization_code()
            );

            List<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_USER")
            );

            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } catch (UnauthorizeError e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
