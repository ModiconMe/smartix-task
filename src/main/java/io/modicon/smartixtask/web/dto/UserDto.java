package io.modicon.smartixtask.web.dto;

import io.modicon.smartixtask.domain.model.Gender;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserDto(
        @NotEmpty(message = "telephone is empty")
        String telephone,

        @NotEmpty(message = "password is empty")
        @Size(min = 8, max = 32, message = "password must be around 8 and 32 characters")
        String password,

        BigDecimal balance,
        String firstName,
        String lastName,
        String patronymic,
        String email,
        Gender gender,
        LocalDate dateOfBirth
) {
}
