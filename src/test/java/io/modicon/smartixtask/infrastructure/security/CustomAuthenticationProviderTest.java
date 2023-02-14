package io.modicon.smartixtask.infrastructure.security;

import io.modicon.smartixtask.infrastructure.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationProviderTest {

    private AuthenticationProvider authenticationProvider;

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        authenticationProvider = new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Test
    void shouldAuthenticateUser() {
        CustomUserDetails userDetails = new CustomUserDetails("telephone", "password");

        when(userDetailsService.loadUserByUsername(userDetails.getUsername())).thenReturn(userDetails);
        when(passwordEncoder.matches(userDetails.password(), userDetails.password())).thenReturn(true);

        Authentication token = authenticationProvider.authenticate(new TestingAuthenticationToken(userDetails, "password"));
        assertEquals(userDetails.password(), token.getCredentials());
        assertEquals(userDetails.getUsername(), token.getPrincipal());
        assertTrue(token.getAuthorities().isEmpty());
    }

    @Test
    void shouldNotAuthenticateUser_whenUserIsNotExist() {
        CustomUserDetails userDetails = new CustomUserDetails("telephone", "password");

        when(userDetailsService.loadUserByUsername(userDetails.getUsername())).thenReturn(null);
        assertThatThrownBy(() -> authenticationProvider.authenticate(new TestingAuthenticationToken(userDetails, "password")))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(String.format("user with username [%s] is not exist", userDetails.getUsername()));
    }

    @Test
    void shouldNotAuthenticateUser_whenUserPasswordIncorrect() {
        CustomUserDetails userDetails = new CustomUserDetails("telephone", "password");

        when(userDetailsService.loadUserByUsername(userDetails.getUsername())).thenReturn(userDetails);
        when(passwordEncoder.matches(userDetails.password(), userDetails.password())).thenReturn(false);

        assertThatThrownBy(() -> authenticationProvider.authenticate(new TestingAuthenticationToken(userDetails, "password")))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(String.format("wrong password for user [%s]", userDetails.getUsername()));
    }
}