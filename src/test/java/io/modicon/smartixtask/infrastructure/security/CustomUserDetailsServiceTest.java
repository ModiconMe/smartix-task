package io.modicon.smartixtask.infrastructure.security;

import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    private UserDetailsService userDetailsService;

    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        userDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void shouldReturnUserDetails() {
        String telephone = "telephone";

        UserEntity user = UserEntity.builder()
                .telephone(telephone)
                .password("password")
                .build();
        UserDetails userDetails = new CustomUserDetails(user.getTelephone(), user.getPassword());

        when(userRepository.findById(telephone)).thenReturn(Optional.of(user));

        UserDetails result = userDetailsService.loadUserByUsername(telephone);

        assertEquals(userDetails, result);
    }

    @Test
    void shouldReturnNull_whenUserIsNotExist() {
        String telephone = "telephone";

        when(userRepository.findById(telephone)).thenReturn(Optional.empty());

        UserDetails result = userDetailsService.loadUserByUsername(telephone);

        assertNull(result);
    }
}