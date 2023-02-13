package io.modicon.smartixtask.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface JwtValidation {
    boolean isTokenValid(String token);

    String extractUsername(String token);

    @RequiredArgsConstructor
    @Component
    class Base implements JwtValidation {

        private final UserDetailsService userDetailsService;
        private final JwtUtils jwtUtils;

        @Override
        public boolean isTokenValid(String token) {
            boolean expired = jwtUtils.isTokenExpired(token);
            Optional<UserDetails> userDetails = Optional.ofNullable(
                    userDetailsService.loadUserByUsername(extractUsername(token)));
            return (userDetails.isPresent() && !expired);
        }

        @Override
        public String extractUsername(String token) {
            Claims claims = jwtUtils.extractClaims(token);
            return claims.getSubject();
        }
    }
}
