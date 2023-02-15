package io.modicon.smartixtask.application.mapper;

import io.modicon.smartixtask.domain.model.Gender;
import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.web.dto.UserDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper = new UserMapper();

    @Test
    void shouldMap() {
        UserEntity user = UserEntity.builder()
                .telephone("telephone")
                .email("email")
                .balance(BigDecimal.valueOf(1000))
                .dateOfBirth(LocalDate.now())
                .firstName("firstName")
                .lastName("lastName")
                .patronymic("patronymic")
                .gender(Gender.MALE)
                .password("password")
                .build();
        UserDto userDto = UserDto.builder()
                .telephone(user.getTelephone())
                .email(user.getEmail())
                .balance(user.getBalance())
                .dateOfBirth(user.getDateOfBirth())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .patronymic(user.getPatronymic())
                .gender(user.getGender())
                .build();

        UserDto result = userMapper.apply(user);

        assertEquals(userDto, result);
    }
}