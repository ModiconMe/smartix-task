package io.modicon.smartixtask.application.service;

import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.domain.repository.PaymentRepository;
import io.modicon.smartixtask.domain.repository.UserRepository;
import io.modicon.smartixtask.infrastructure.exception.ApiException;
import io.modicon.smartixtask.infrastructure.security.CustomUserDetails;
import io.modicon.smartixtask.web.dto.PaymentRequest;
import io.modicon.smartixtask.web.dto.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private PaymentService paymentService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService.Base(userRepository, paymentRepository);
    }

    @Test
    void shouldPaySuccessfully() {
        UserEntity payer = UserEntity.builder()
                .telephone("payer_id")
                .password("payer_password")
                .balance(BigDecimal.valueOf(1000))
                .build();

        UserEntity payee = UserEntity.builder()
                .telephone("payee_id")
                .password("payee_password")
                .balance(BigDecimal.valueOf(2000))
                .build();

        when(userRepository.findById(payer.getTelephone())).thenReturn(Optional.of(payer));
        when(userRepository.findById(payee.getTelephone())).thenReturn(Optional.of(payee));

        BigDecimal amount = BigDecimal.valueOf(500);
        PaymentResponse result = paymentService.pay(new PaymentRequest(payee.getTelephone(), amount),
                new CustomUserDetails(payer.getTelephone(), payer.getPassword()));

        assertEquals(result.getMessage(), String.format("user [%s] successfully pay to user [%s] [%s] rub",
                payer.getTelephone(), payee.getTelephone(), amount));
    }

    @Test
    void shouldNotPay_whenBadCredentials() {
        UserEntity payer = UserEntity.builder()
                .telephone("payer_id")
                .password("payer_password")
                .balance(BigDecimal.valueOf(1000))
                .build();

        UserEntity payee = UserEntity.builder()
                .telephone("payee_id")
                .password("payee_password")
                .balance(BigDecimal.valueOf(2000))
                .build();

        when(userRepository.findById(payer.getTelephone())).thenReturn(Optional.empty());
        BigDecimal amount = BigDecimal.valueOf(500);
        assertThatThrownBy(() -> paymentService.pay(new PaymentRequest(payee.getTelephone(), amount),
                new CustomUserDetails(payer.getTelephone(), payer.getPassword())))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(String.format("user with telephone number [%s] not found", payer.getTelephone()));
    }

    @Test
    void shouldNotPay_whenPayeeNotExist() {
        UserEntity payer = UserEntity.builder()
                .telephone("payer_id")
                .password("payer_password")
                .balance(BigDecimal.valueOf(1000))
                .build();

        UserEntity payee = UserEntity.builder()
                .telephone("payee_id")
                .password("payee_password")
                .balance(BigDecimal.valueOf(2000))
                .build();

        when(userRepository.findById(payer.getTelephone())).thenReturn(Optional.of(payer));
        when(userRepository.findById(payee.getTelephone())).thenReturn(Optional.empty());
        BigDecimal amount = BigDecimal.valueOf(500);
        assertThatThrownBy(() -> paymentService.pay(new PaymentRequest(payee.getTelephone(), amount),
                new CustomUserDetails(payer.getTelephone(), payer.getPassword())))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(String.format("user with telephone number [%s] not found", payee.getTelephone()));
    }

    @Test
    void shouldNotPay_whenNotEnoughMoney() {
        UserEntity payer = UserEntity.builder()
                .telephone("payer_id")
                .password("payer_password")
                .balance(BigDecimal.valueOf(1000))
                .build();

        UserEntity payee = UserEntity.builder()
                .telephone("payee_id")
                .password("payee_password")
                .balance(BigDecimal.valueOf(2000))
                .build();

        when(userRepository.findById(payer.getTelephone())).thenReturn(Optional.of(payer));
        when(userRepository.findById(payee.getTelephone())).thenReturn(Optional.of(payee));
        BigDecimal amount = BigDecimal.valueOf(1500);
        assertThatThrownBy(() -> paymentService.pay(new PaymentRequest(payee.getTelephone(), amount),
                new CustomUserDetails(payer.getTelephone(), payer.getPassword())))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(String.format("user [%s] has not enough money", payer.getTelephone()));
    }
}