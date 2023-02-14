package io.modicon.smartixtask.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.modicon.smartixtask.domain.model.Gender;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
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
