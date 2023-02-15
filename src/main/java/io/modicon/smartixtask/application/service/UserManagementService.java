package io.modicon.smartixtask.application.service;

import io.modicon.smartixtask.application.mapper.UserMapper;
import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.domain.repository.UserRepository;
import io.modicon.smartixtask.infrastructure.security.CustomUserDetails;
import io.modicon.smartixtask.infrastructure.security.jwt.JwtGeneration;
import io.modicon.smartixtask.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static io.modicon.smartixtask.infrastructure.exception.ApiException.exception;

public interface UserManagementService {

    UserRegisterResponse register(UserRegisterRequest request);

    UserBalanceResponse getBalance(UserDetails currentUser);

    UserUpdateResponse updateUser(UserUpdateRequest request, UserDetails currentUser);

    @Transactional
    @Slf4j
    @RequiredArgsConstructor
    @Service
    class Base implements UserManagementService {

        private final UserRepository userRepository;
        private final UserMapper userMapper;
        private final PasswordEncoder passwordEncoder;
        private final JwtGeneration jwtGeneration;
        private final PhoneValidationService phoneValidationService;

        @Override
        public UserRegisterResponse register(UserRegisterRequest request) {
            String telephone = request.getTelephone();
            if (userRepository.existsById(telephone))
                throw exception(HttpStatus.BAD_REQUEST, "user with telephone number [%s] already exist", telephone);

            if (!phoneValidationService.isValidPhoneNumber(telephone, "RU"))
                throw exception(HttpStatus.BAD_REQUEST, "phone number [%s] is invalid", telephone);

            UserEntity user = UserEntity.builder()
                    .telephone(telephone)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .balance(BigDecimal.valueOf(1000))
                    .build();

            userRepository.save(user);
            log.info("register user {}", user);

            String token = jwtGeneration.generateAccessToken(new CustomUserDetails(telephone, user.getPassword()));

            return new UserRegisterResponse(userMapper.apply(user), token);
        }

        @Transactional(readOnly = true)
        @Override
        public UserBalanceResponse getBalance(UserDetails currentUser) {
            String telephone = currentUser.getUsername();
            UserEntity user = userRepository.findById(telephone)
                    .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "user with telephone number [%s] not found", telephone));
            return new UserBalanceResponse(telephone, user.getBalance());
        }

        @Override
        public UserUpdateResponse updateUser(UserUpdateRequest request, UserDetails currentUser) {
            String telephone = currentUser.getUsername();
            UserEntity user = userRepository.findById(telephone).orElseThrow(() ->
                    exception(HttpStatus.NOT_FOUND, "user with telephone number [%s] not found", telephone));

            UserEntity updatedUser = userRepository.save(user.toBuilder()
                    .email(request.getEmail() != null ? request.getEmail() : user.getEmail())
                    .firstName(request.getFirstName() != null ? request.getFirstName() : user.getFirstName())
                    .lastName(request.getLastName() != null ? request.getLastName() : user.getLastName())
                    .patronymic(request.getPatronymic() != null ? request.getPatronymic() : user.getPatronymic())
                    .dateOfBirth(request.getDateOfBirth() != null ? request.getDateOfBirth() : user.getDateOfBirth())
                    .gender(request.getGender() != null ? request.getGender() : user.getGender())
                    .build());

            log.info("user {} updated, new user: {}", telephone, updatedUser);
            return new UserUpdateResponse(userMapper.apply(updatedUser));
        }
    }
}
