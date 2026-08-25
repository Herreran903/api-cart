package com.api_cart.cart.infra.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.api_cart.cart.domain.util.GlobalConstants.HEADER_STRING;
import static com.api_cart.cart.domain.util.GlobalConstants.TOKEN_PREFIX;
import static com.api_cart.cart.infra.security.jwt.JwtUtility.extractJwt;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HEADER_STRING);

        if (isValidAuthHeader(authHeader)) {
            String jwt = extractJwt(authHeader);
            String email = jwtService.extractUsername(jwt);
            List<GrantedAuthority> authorities = List.of(jwtService.extractAuthorities(jwt));

            if (!isAlreadyAuthenticated()) {
                UserDetails user = new User(email, "", authorities);
                setAuthentication(user, request);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidAuthHeader(String authHeader) {
        return authHeader != null && authHeader.startsWith(TOKEN_PREFIX);
    }

    private boolean isAlreadyAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    private void setAuthentication(UserDetails user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}