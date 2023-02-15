package io.modicon.smartixtask.application.mapper;

import io.modicon.smartixtask.domain.model.PaymentEntity;
import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.web.dto.PaymentDto;
import io.modicon.smartixtask.web.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PaymentMapper implements Function<PaymentEntity, PaymentDto> {

    @Override
    public PaymentDto apply(PaymentEntity payment) {
        return new PaymentDto(payment.getCreatedAt(), payment.getAmount(),
                payment.getPayer().getTelephone());
    }
}
