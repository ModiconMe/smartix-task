package io.modicon.smartixtask.application.service;

import io.modicon.smartixtask.domain.model.PaymentEntity;
import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.domain.repository.PaymentRepository;
import io.modicon.smartixtask.domain.repository.UserRepository;
import io.modicon.smartixtask.infrastructure.exception.ApiException;
import io.modicon.smartixtask.infrastructure.security.CustomUserDetails;
import io.modicon.smartixtask.web.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        UserDto payerExpected = UserDto.builder()
                .telephone(payer.getTelephone())
                .balance(payer.getBalance().subtract(amount))
                .build();
        UserDto payeeExpected = UserDto.builder()
                .telephone(payee.getTelephone())
                .balance(payee.getBalance().add(amount))
                .build();

        assertEquals(result.getPayer(), payerExpected);
        assertEquals(result.getPayee(), payeeExpected);
        assertEquals(result.getAmount(), amount);
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

    @Test
    void shouldGetUserPayments() {
        UserEntity user1 = UserEntity.builder()
                .telephone("telephone1")
                .password("password1")
                .balance(BigDecimal.valueOf(1000))
                .build();
        UserEntity user2 = UserEntity.builder()
                .telephone("telephone1")
                .password("password1")
                .balance(BigDecimal.valueOf(1000))
                .build();

        PaymentEntity payment1 = PaymentEntity.builder()
                .id(UUID.randomUUID().toString())
                .amount(BigDecimal.valueOf(10))
                .payer(user1)
                .payee(user2)
                .createdAt(ZonedDateTime.now())
                .build();
        PaymentEntity payment2 = PaymentEntity.builder()
                .id(UUID.randomUUID().toString())
                .amount(BigDecimal.valueOf(10))
                .payer(user1)
                .payee(user2)
                .createdAt(ZonedDateTime.now())
                .build();

        when(userRepository.findById(user1.getTelephone())).thenReturn(Optional.of(user1));
        int page = 0;
        int limit = 2;
        when(paymentRepository.findByPayee(user1, PageRequest.of(page, limit))).thenReturn(List.of(payment1, payment2));

        UserPaymentsResponse result = paymentService.getUserPayment(String.valueOf(limit), String.valueOf(page),
                new CustomUserDetails(user1.getTelephone(), user1.getPassword()));

        PaymentDto paymentDto1 = new PaymentDto(payment1.getCreatedAt(), payment1.getAmount(), payment1.getPayer().getTelephone());
        PaymentDto paymentDto2 = new PaymentDto(payment2.getCreatedAt(), payment2.getAmount(), payment2.getPayer().getTelephone());

        List<PaymentDto> expected = List.of(paymentDto1, paymentDto2);

        assertEquals(result.getPayments(), expected);
    }

    @Test
    void shouldNotGetUserPayments_whenUserIsNotExist() {
        String telephone = "telephone";
        String password = "password";

        when(userRepository.findById(telephone)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.getUserPayment("1", "0", new CustomUserDetails(telephone, password)))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("user with telephone number [%s] not found", telephone);
    }
}