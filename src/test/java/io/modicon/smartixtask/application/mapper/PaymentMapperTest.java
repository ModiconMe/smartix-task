package io.modicon.smartixtask.application.mapper;

import io.modicon.smartixtask.domain.model.PaymentEntity;
import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.web.dto.PaymentDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMapperTest {

    private PaymentMapper paymentMapper = new PaymentMapper();

    @Test
    void shouldMap() {
        UserEntity payer = UserEntity.builder()
                .telephone("payer")
                .build();
        UserEntity payee = UserEntity.builder()
                .telephone("payee")
                .build();

        ZonedDateTime createdAt = ZonedDateTime.now();
        BigDecimal amount = BigDecimal.valueOf(100);
        PaymentEntity payment = PaymentEntity.builder()
                .id(UUID.randomUUID().toString())
                .amount(amount)
                .createdAt(createdAt)
                .payee(payee)
                .payer(payer)
                .build();
        PaymentDto paymentDto = new PaymentDto(createdAt, amount, payer.getTelephone());

        PaymentDto result = paymentMapper.apply(payment);

        assertEquals(result, paymentDto);
    }
}