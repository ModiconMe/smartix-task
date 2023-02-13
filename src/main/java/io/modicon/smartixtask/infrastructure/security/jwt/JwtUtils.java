package io.modicon.smartixtask.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

public interface JwtUtils {
    boolean isTokenExpired(String token);

    Claims extractClaims(String token);

    @RequiredArgsConstructor
    @Component
    class Base implements JwtUtils {
        private final JwtConfig jwtConfig;

        @Override
        public boolean isTokenExpired(String token) {
            Claims claims = extractClaims(token);
            Instant now = Instant.now();
            Date exp = claims.getExpiration();
            return exp.before(Date.from(now));
        }

        @Override
        public Claims extractClaims(String token) {
            return Jwts.parserBuilder().setSigningKey(jwtConfig.getKey()).build().parseClaimsJws(token).getBody();
        }
    }
}
