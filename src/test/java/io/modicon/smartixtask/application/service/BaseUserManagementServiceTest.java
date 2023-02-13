package io.modicon.smartixtask.application.service;

import io.modicon.smartixtask.domain.model.Gender;
import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.domain.repository.UserRepository;
import io.modicon.smartixtask.infrastructure.exception.ApiException;
import io.modicon.smartixtask.infrastructure.security.CustomUserDetails;
import io.modicon.smartixtask.infrastructure.security.jwt.JwtGeneration;
import io.modicon.smartixtask.web.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BaseUserManagementServiceTest {

    private UserManagementService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtGeneration jwtGeneration;

    @Mock
    private PhoneValidationService phoneValidationService;

    @BeforeEach
    void setUp() {
        userService = new UserManagementService.Base(userRepository, passwordEncoder, jwtGeneration, phoneValidationService);
    }

    @Test
    void shouldRegisterUser() {
        String telephone = "telephone";
        String password = "password";

        when(userRepository.existsById(telephone)).thenReturn(false);
        when(phoneValidationService.isValidPhoneNumber(telephone, "RU")).thenReturn(true);
        when(passwordEncoder.encode(password)).thenReturn(password);
        String token = "token";
        when(jwtGeneration.generateAccessToken(new CustomUserDetails(telephone, password))).thenReturn(token);

        UserRegisterResponse result = userService.register(new UserRegisterRequest(telephone, password));

        assertEquals(result.getTelephone(), telephone);
        assertEquals(result.getToken(), token);
    }

    @Test
    void shouldNotRegisterUser_whenTelephoneAlreadyExist() {
        String telephone = "telephone";
        String password = "password";

        when(userRepository.existsById(telephone)).thenReturn(true);

        assertThatThrownBy(() -> userService.register(new UserRegisterRequest(telephone, password)))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(String.format("user with telephone number [%s] already exist", telephone));
    }

    @Test
    void shouldNotRegisterUser_whenTelephoneIsInvalid() {
        String telephone = "telephone";
        String password = "password";

        when(userRepository.existsById(telephone)).thenReturn(false);
        when(phoneValidationService.isValidPhoneNumber(telephone, "RU")).thenReturn(false);

        assertThatThrownBy(() -> userService.register(new UserRegisterRequest(telephone, password)))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(String.format("phone number [%s] is invalid", telephone));
    }

    @Test
    void shouldUpdateUser() {
        String telephone = "telephone";
        String password = "password";
        UserEntity user = UserEntity.builder()
                .telephone(telephone)
                .password(password)
                .build();

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("firstName", "lastName", "patronymic",
                "email", Gender.MALE, LocalDate.of(1999, 7, 9));

        UserEntity updatedUser = UserEntity.builder()
                .telephone(telephone)
                .password(password)
                .firstName(userUpdateRequest.getFirstName())
                .lastName(userUpdateRequest.getLastName())
                .patronymic(userUpdateRequest.getPatronymic())
                .email(userUpdateRequest.getEmail())
                .gender(userUpdateRequest.getGender())
                .dateOfBirth(userUpdateRequest.getDateOfBirth())
                .build();

        when(userRepository.findById(telephone)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);

        UserUpdateResponse result = userService.updateUser(userUpdateRequest, new CustomUserDetails(telephone, password));
        UserUpdateResponse expected = new UserUpdateResponse(UserDto.builder()
                .telephone(telephone)
                .firstName(userUpdateRequest.getFirstName())
                .lastName(userUpdateRequest.getLastName())
                .patronymic(userUpdateRequest.getPatronymic())
                .email(userUpdateRequest.getEmail())
                .dateOfBirth(userUpdateRequest.getDateOfBirth())
                .gender(userUpdateRequest.getGender())
                .balance(user.getBalance())
                .build());

        assertEquals(result, expected);
    }

    @Test
    void shouldNotUpdateUser_whenUserIsNotExist() {
        String telephone = "telephone";
        String password = "password";

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("firstName", "lastName", "patronymic",
                "email", Gender.MALE, LocalDate.of(1999, 7, 9));

        when(userRepository.findById(telephone)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(userUpdateRequest, new CustomUserDetails(telephone, password)))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("user with telephone number [%s] not found", telephone);
    }
}