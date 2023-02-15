package io.modicon.smartixtask.application.service;

import io.modicon.smartixtask.application.mapper.UserMapper;
import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.domain.repository.UserRepository;
import io.modicon.smartixtask.infrastructure.exception.ApiException;
import io.modicon.smartixtask.infrastructure.security.CustomUserDetails;
import io.modicon.smartixtask.infrastructure.security.jwt.JwtGeneration;
import io.modicon.smartixtask.web.dto.UserDto;
import io.modicon.smartixtask.web.dto.UserLoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccessServiceTest {

    private UserAccessService userAccessService;

    @Mock
    private JwtGeneration jwtGeneration;
    @Mock
    private UserRepository userRepository;
    private UserMapper userMapper = new UserMapper();

    @BeforeEach
    void setUp() {
        userAccessService = new UserAccessService.Base(jwtGeneration, userRepository, userMapper);
    }

    @Test
    void shouldLoginUser() {
        String telephone = "telephone";
        String password = "password";
        UserDetails currentUser = new CustomUserDetails(telephone, password);
        UserEntity user = UserEntity.builder()
                .telephone(telephone)
                .password(password)
                .build();
        UserDto userDto = UserDto.builder()
                .telephone(telephone)
                .build();

        String token = "token";
        when(jwtGeneration.generateAccessToken(currentUser)).thenReturn(token);
        when(userRepository.findById(telephone)).thenReturn(Optional.of(user));

        UserLoginResponse result = userAccessService.login(currentUser);

        assertEquals(result.getUser(), userDto);
        assertEquals(result.getToken(), token);
    }

    @Test
    void shouldNotLoginUser_whenUserIsNotExist() {
        String telephone = "telephone";
        String password = "password";
        UserDetails currentUser = new CustomUserDetails(telephone, password);

        when(userRepository.findById(telephone)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userAccessService.login(currentUser))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("user with telephone number [%s] not found", telephone);
    }
}