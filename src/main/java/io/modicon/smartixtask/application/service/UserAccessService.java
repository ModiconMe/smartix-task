package io.modicon.smartixtask.application.service;

import io.modicon.smartixtask.application.mapper.UserMapper;
import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.domain.repository.UserRepository;
import io.modicon.smartixtask.infrastructure.security.jwt.JwtGeneration;
import io.modicon.smartixtask.web.dto.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static io.modicon.smartixtask.infrastructure.exception.ApiException.exception;

public interface UserAccessService {

    UserLoginResponse login(UserDetails currentUser);

    @RequiredArgsConstructor
    @Service
    class Base implements UserAccessService {

        private final JwtGeneration jwtGeneration;
        private final UserRepository userRepository;
        private final UserMapper userMapper;

        @Override
        public UserLoginResponse login(UserDetails currentUser) {
            String telephone = currentUser.getUsername();
            UserEntity user = userRepository.findById(telephone)
                    .orElseThrow(() -> exception(HttpStatus.NOT_FOUND,
                            "user with telephone number [%s] not found", telephone));

            String token = jwtGeneration.generateAccessToken(currentUser);
            return new UserLoginResponse(userMapper.apply(user), token);
        }
    }
}
