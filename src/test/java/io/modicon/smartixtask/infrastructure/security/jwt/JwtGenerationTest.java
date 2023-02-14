package io.modicon.smartixtask.infrastructure.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.modicon.smartixtask.infrastructure.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtGenerationTest {

    private JwtGeneration jwtGeneration;

    @Mock
    private JwtConfig jwtConfig;

    @BeforeEach
    void setUp() {
        jwtGeneration = new JwtGeneration.Base(jwtConfig);
    }

    @Test
    void shouldGenerateJwtKey() {
        CustomUserDetails userDetails = new CustomUserDetails("telephone", "password");

        String issuer = "issuer";
        Date issueAt = new Date();
        Date accessExpired = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
        String signKey = "secretsecretsecretsecretsecretsecret";
        when(jwtConfig.getIssuer()).thenReturn(issuer);
        when(jwtConfig.getKey()).thenReturn(Keys.hmacShaKeyFor(signKey.getBytes()));
        when(jwtConfig.getTokenExpiredTime()).thenReturn(accessExpired);
        when(jwtConfig.getIssueAt()).thenReturn(issueAt);

        Map<String, Object> claims = new HashMap<>();
        String expected = Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuer(issuer)
                .claim("authorities", userDetails.getAuthorities())
                .setIssuedAt(jwtConfig.getIssueAt())
                .setExpiration(jwtConfig.getTokenExpiredTime())
                .signWith(jwtConfig.getKey()).compact();

        String token = jwtGeneration.generateAccessToken(userDetails);

        assertFalse(token.isEmpty());
        assertEquals(token, expected);
    }
}