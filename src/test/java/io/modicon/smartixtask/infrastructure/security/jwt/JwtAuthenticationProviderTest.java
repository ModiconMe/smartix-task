package io.modicon.smartixtask.infrastructure.security.jwt;

import io.modicon.smartixtask.infrastructure.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationProviderTest {

    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        jwtAuthenticationProvider = new JwtAuthenticationProvider.Base(userDetailsService);
    }

    @Test
    void shouldAuthenticate() {
        String telephone = "telephone";
        CustomUserDetails userDetails = new CustomUserDetails(telephone, "password");

        when(userDetailsService.loadUserByUsername(telephone)).thenReturn(userDetails);

        Authentication token = jwtAuthenticationProvider.getAuthentication(telephone);

        assertEquals(userDetails.password(), token.getCredentials());
        assertEquals(userDetails, token.getPrincipal());
        assertTrue(token.getAuthorities().isEmpty());
    }

    @Test
    void shouldNotAuthenticate_whenUserIsNotExist() {
        String telephone = "telephone";

        when(userDetailsService.loadUserByUsername(telephone)).thenReturn(null);

        Authentication token = jwtAuthenticationProvider.getAuthentication(telephone);

        assertNull(token);
    }
}