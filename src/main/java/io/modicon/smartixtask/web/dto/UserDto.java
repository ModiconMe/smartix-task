package io.modicon.smartixtask.web.dto;

import java.math.BigDecimal;

public record UserDto(
        String telephone,
        BigDecimal balance,
        String firstName,
        String patronymic
) {
}
