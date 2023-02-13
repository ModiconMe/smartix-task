package io.modicon.smartixtask.web.dto;

import io.modicon.smartixtask.domain.model.Gender;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record UserDto(
        String telephone,
        BigDecimal balance,
        String firstName,
        String lastName,
        String patronymic,
        String email,
        Gender gender,
        LocalDate dateOfBirth
) {
}
