package io.modicon.smartixtask.domain.repository;

import io.modicon.smartixtask.domain.model.PaymentEntity;
import io.modicon.smartixtask.domain.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnUserPayment() {
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

        userRepository.save(user1);
        userRepository.save(user2);

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
        PaymentEntity payment3 = PaymentEntity.builder()
                .id(UUID.randomUUID().toString())
                .amount(BigDecimal.valueOf(10))
                .payer(user1)
                .payee(user2)
                .createdAt(ZonedDateTime.now())
                .build();
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(payment3);

        int limit = 2;
        List<PaymentEntity> result = paymentRepository.findByPayee(user2, PageRequest.of(0, limit));

        assertEquals(result.size(), limit);
        assertEquals(result, List.of(payment1, payment2));
    }

    @Test
    void shouldNotReturnUserPayment_whenPageTooBig() {
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

        userRepository.save(user1);
        userRepository.save(user2);

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
        PaymentEntity payment3 = PaymentEntity.builder()
                .id(UUID.randomUUID().toString())
                .amount(BigDecimal.valueOf(10))
                .payer(user1)
                .payee(user2)
                .createdAt(ZonedDateTime.now())
                .build();
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(payment3);

        int limit = 2;
        List<PaymentEntity> result = paymentRepository.findByPayee(user2, PageRequest.of(2, limit));

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldNotReturnUserPayment_whenPaymentsIsNotExist() {
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

        userRepository.save(user1);
        userRepository.save(user2);

        int limit = 2;
        List<PaymentEntity> result = paymentRepository.findByPayee(user1, PageRequest.of(0, limit));

        assertTrue(result.isEmpty());
    }
}