package io.modicon.smartixtask.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.modicon.smartixtask.application.service.SecurityContextHolderService;
import io.modicon.smartixtask.application.service.UserAccessService;
import io.modicon.smartixtask.application.service.UserManagementService;
import io.modicon.smartixtask.domain.model.Gender;
import io.modicon.smartixtask.infrastructure.security.CustomUserDetails;
import io.modicon.smartixtask.infrastructure.security.jwt.JwtAuthFilter;
import io.modicon.smartixtask.web.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAccessService userAccessService;
    @MockBean
    private SecurityContextHolderService securityContextHolderService;
    @MockBean
    private UserManagementService userManagementService;
    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private final static String BASE_URL = "/api/v1/users";

    @Test
    void shouldRegisterUser() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest("telephone", "password");
        String token = "token";
        UserRegisterResponse response = new UserRegisterResponse(request.getTelephone(), token);

        when(userManagementService.register(request)).thenReturn(response);

        var json = mockMvc.perform(post(BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Mockito.verify(userManagementService, times(1)).register(request);
        var result = objectMapper.writeValueAsString(response);
        assertEquals(result, json);
    }

    @Test
    void shouldLoginUser() throws Exception {
        CustomUserDetails currentUser = new CustomUserDetails("telephone", "password");
        when(securityContextHolderService.getCurrentUser()).thenReturn(currentUser);

        String token = "token";
        UserLoginResponse response = new UserLoginResponse(currentUser.getUsername(), token);

        when(userAccessService.login(currentUser)).thenReturn(response);

        var json = mockMvc.perform(post(BASE_URL + "/login"))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Mockito.verify(userAccessService, times(1)).login(currentUser);
        var result = objectMapper.writeValueAsString(response);
        assertEquals(result, json);
    }

    @Test
    void shouldGetUserBalance() throws Exception {
        CustomUserDetails currentUser = new CustomUserDetails("telephone", "password");
        when(securityContextHolderService.getCurrentUser()).thenReturn(currentUser);

        UserBalanceResponse response = new UserBalanceResponse(currentUser.telephone(), BigDecimal.valueOf(1000));

        when(userManagementService.getBalance(currentUser)).thenReturn(response);

        var json = mockMvc.perform(get(BASE_URL + "/balance"))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Mockito.verify(userManagementService, times(1)).getBalance(currentUser);
        var result = objectMapper.writeValueAsString(response);
        assertEquals(result, json);
    }

    @Test
    void shouldUpdateUser() throws Exception {
        CustomUserDetails currentUser = new CustomUserDetails("telephone", "password");
        when(securityContextHolderService.getCurrentUser()).thenReturn(currentUser);

        UserUpdateRequest request = new UserUpdateRequest("firstName", "lastName", "patronymic", "email@mail.ru", Gender.MALE, LocalDate.of(1999, 7, 9));
        UserDto user = UserDto.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .patronymic(request.getPatronymic())
                .email(request.getEmail())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .build();

        UserUpdateResponse response = new UserUpdateResponse(user);

        when(userManagementService.updateUser(request, currentUser)).thenReturn(response);

        var json = mockMvc.perform(put(BASE_URL + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Mockito.verify(userManagementService, times(1)).updateUser(request, currentUser);
        var result = objectMapper.writeValueAsString(response);
        assertEquals(result, json);
    }
}