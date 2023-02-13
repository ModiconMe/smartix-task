package io.modicon.smartixtask.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserPaymentsResponse {
    List<PaymentDto> payments;
}
