package io.modicon.smartixtask.application.service;

import io.modicon.smartixtask.infrastructure.security.CustomUserDetails;
import io.modicon.smartixtask.infrastructure.security.jwt.JwtGeneration;
import io.modicon.smartixtask.web.dto.UserLoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccessServiceTest {

    private UserAccessService userAccessService;

    @Mock
    private JwtGeneration jwtGeneration;

    @BeforeEach
    void setUp() {
        userAccessService = new UserAccessService.Base(jwtGeneration);
    }

    @Test
    void shouldLoginUser() {
        String telephone = "telephone";
        String password = "password";
        UserDetails user = new CustomUserDetails(telephone, password);

        String token = "token";
        when(jwtGeneration.generateAccessToken(user)).thenReturn(token);

        UserLoginResponse result = userAccessService.login(user);

        assertEquals(result.getTelephone(), telephone);
        assertEquals(result.getToken(), token);
    }
}