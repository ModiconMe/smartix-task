package io.modicon.smartixtask.application.service;

import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.web.dto.UserDto;

public class UserMapper {
    public static UserDto mapToDto(UserEntity user) {
        return new UserDto(user.getTelephone(),
                user.getBalance(), user.getFirstName(), user.getPatronymic());
    }
}
