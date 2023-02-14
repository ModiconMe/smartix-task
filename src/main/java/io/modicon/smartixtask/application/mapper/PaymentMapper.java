package io.modicon.smartixtask.application.mapper;

import io.modicon.smartixtask.domain.model.PaymentEntity;
import io.modicon.smartixtask.web.dto.PaymentDto;

public class PaymentMapper {
    public static PaymentDto mapToDto(PaymentEntity payment) {
        return new PaymentDto(payment.getCreatedAt(), payment.getAmount(),
                payment.getPayer().getTelephone());
    }
}
