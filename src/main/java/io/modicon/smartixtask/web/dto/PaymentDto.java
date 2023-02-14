package io.modicon.smartixtask.web.dto;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record PaymentDto(
        ZonedDateTime createdAt,
        BigDecimal amount,
        String payer
) {
}
