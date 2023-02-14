package io.modicon.smartixtask.infrastructure.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.modicon.smartixtask.infrastructure.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtValidationTest {

    private JwtValidation jwtValidation;

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtConfig jwtConfig;

    @BeforeEach
    void setUp() {
        jwtValidation = new JwtValidation.Base(userDetailsService, jwtConfig);
    }

    @Test
    void shouldReturnTrue_whenTokenIsValid() {
        CustomUserDetails userDetails = new CustomUserDetails("telephone", "password");

        String issuer = "issuer";
        Date issueAt = new Date();
        Date accessExpired = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
        String signKey = "secretsecretsecretsecretsecretsecret";

        Map<String, Object> claims = new HashMap<>();
        SecretKey key = Keys.hmacShaKeyFor(signKey.getBytes());
        String token = Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuer(issuer)
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(issueAt)
                .setExpiration(accessExpired)
                .signWith(key).compact();

        when(jwtConfig.getKey()).thenReturn(key);
        when(userDetailsService.loadUserByUsername(userDetails.getUsername())).thenReturn(userDetails);

        boolean result = jwtValidation.isTokenValid(token);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalse_whenTokenIsExpired() {
        CustomUserDetails userDetails = new CustomUserDetails("telephone", "password");

        String issuer = "issuer";
        Date issueAt = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1));
        Date accessExpired = new Date(System.currentTimeMillis());
        String signKey = "secretsecretsecretsecretsecretsecret";

        Map<String, Object> claims = new HashMap<>();
        SecretKey key = Keys.hmacShaKeyFor(signKey.getBytes());
        String token = Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuer(issuer)
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(issueAt)
                .setExpiration(accessExpired)
                .signWith(key).compact();

        when(jwtConfig.getKey()).thenReturn(key);

        boolean result = jwtValidation.isTokenValid(token);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalse_whenUserIsNotExist() {
        CustomUserDetails userDetails = new CustomUserDetails("telephone", "password");

        String issuer = "issuer";
        Date issueAt = new Date(System.currentTimeMillis());
        Date accessExpired = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
        String signKey = "secretsecretsecretsecretsecretsecret";

        Map<String, Object> claims = new HashMap<>();
        SecretKey key = Keys.hmacShaKeyFor(signKey.getBytes());
        String token = Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuer(issuer)
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(issueAt)
                .setExpiration(accessExpired)
                .signWith(key).compact();

        when(jwtConfig.getKey()).thenReturn(key);
        when(userDetailsService.loadUserByUsername(userDetails.getUsername())).thenReturn(null);

        boolean result = jwtValidation.isTokenValid(token);

        assertFalse(result);
    }
}