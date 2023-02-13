package io.modicon.smartixtask.application.mapper;

import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.web.dto.UserDto;

public class UserMapper {
    public static UserDto mapToDto(UserEntity user) {
        return UserDto.builder()
                .telephone(user.getTelephone())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .patronymic(user.getPatronymic())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .balance(user.getBalance())
                .gender(user.getGender())
                .build();
    }
}
