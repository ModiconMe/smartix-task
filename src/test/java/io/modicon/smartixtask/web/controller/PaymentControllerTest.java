package io.modicon.smartixtask.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.modicon.smartixtask.application.service.PaymentService;
import io.modicon.smartixtask.application.service.SecurityContextHolderService;
import io.modicon.smartixtask.application.service.UserAccessService;
import io.modicon.smartixtask.application.service.UserManagementService;
import io.modicon.smartixtask.domain.model.PaymentEntity;
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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;
    @MockBean
    private SecurityContextHolderService securityContextHolderService;
    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private final static String BASE_URL = "/api/v1/payments";

    @Test
    void shouldPay() throws Exception {
        String payerTelephone = "payerTelephone";
        String payeeTelephone = "payeeTelephone";
        BigDecimal amount = BigDecimal.valueOf(100);
        PaymentRequest request = new PaymentRequest(payeeTelephone, amount);
        CustomUserDetails currentUser = new CustomUserDetails(payerTelephone, "password");
        when(securityContextHolderService.getCurrentUser()).thenReturn(currentUser);

        UserDto payer = UserDto.builder()
                .telephone(payerTelephone)
                .build();
        UserDto payee = UserDto.builder()
                .telephone(payeeTelephone)
                .build();

        PaymentResponse response = new PaymentResponse(payer, payee, amount);

        when(paymentService.pay(request, currentUser)).thenReturn(response);

        var json = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Mockito.verify(paymentService, times(1)).pay(request, currentUser);
        var result = objectMapper.writeValueAsString(response);
        assertEquals(result, json);
    }

    @Test
    void shouldGetUserPayments() throws Exception {
        String payerTelephone = "payerTelephone";
        CustomUserDetails currentUser = new CustomUserDetails(payerTelephone, "password");
        when(securityContextHolderService.getCurrentUser()).thenReturn(currentUser);

        PaymentDto paymentDto1 = new PaymentDto(ZonedDateTime.now(), BigDecimal.valueOf(100), payerTelephone);
        PaymentDto paymentDto2 = new PaymentDto(ZonedDateTime.now(), BigDecimal.valueOf(200), payerTelephone);

        UserPaymentsResponse response = new UserPaymentsResponse(List.of(paymentDto1, paymentDto2));

        String limit = "2";
        String page = "0";
        when(paymentService.getUserPayment(limit, page, currentUser)).thenReturn(response);

        var json = mockMvc.perform(get(BASE_URL)
                        .param("limit", limit)
                        .param("page", page))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        Mockito.verify(paymentService, times(1)).getUserPayment(limit, page, currentUser);
        var result = objectMapper.writeValueAsString(response);
        assertEquals(result, json);
    }
}