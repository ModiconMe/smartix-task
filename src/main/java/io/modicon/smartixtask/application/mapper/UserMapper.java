package io.modicon.smartixtask.application.mapper;

import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.web.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserMapper implements Function<UserEntity, UserDto> {

    @Override
    public UserDto apply(UserEntity user) {
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
