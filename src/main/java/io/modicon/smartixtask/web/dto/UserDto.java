package io.modicon.smartixtask.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.modicon.smartixtask.domain.model.Gender;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Builder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonTypeName("user")
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
