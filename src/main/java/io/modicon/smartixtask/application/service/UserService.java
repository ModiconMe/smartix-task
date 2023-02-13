package io.modicon.smartixtask.application.service;

import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.domain.repository.UserRepository;
import io.modicon.smartixtask.infrastructure.exception.ApiException;
import io.modicon.smartixtask.web.dto.UserRegisterRequest;
import io.modicon.smartixtask.web.dto.UserRegisterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.modicon.smartixtask.infrastructure.exception.ApiException.exception;

public interface UserService {

    UserRegisterResponse register(UserRegisterRequest request);

    @Transactional
    @Slf4j
    @RequiredArgsConstructor
    @Service
    class Base implements UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public UserRegisterResponse register(UserRegisterRequest request) {
            String telephone = request.getTelephone();
            if (userRepository.findById(telephone).isPresent())
                throw exception(HttpStatus.BAD_REQUEST,
                        "user with telephone number [%s] already exist", telephone);

            //TODO phone number validation

            UserEntity user = UserEntity.builder()
                    .telephone(telephone)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            userRepository.save(user);
            log.info("register user {}", user);

            // TODO
            String token = "token";

            return new UserRegisterResponse(telephone, "");
        }
    }
}
