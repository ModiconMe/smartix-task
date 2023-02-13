package io.modicon.smartixtask.application.service;

import io.modicon.smartixtask.infrastructure.security.jwt.JwtGeneration;
import io.modicon.smartixtask.web.dto.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

public interface UserAccessService {

    UserLoginResponse login(UserDetails user);

    @RequiredArgsConstructor
    @Service
    class Base implements UserAccessService {

        private final JwtGeneration jwtGeneration;

        @Override
        public UserLoginResponse login(UserDetails user) {
            String token = jwtGeneration.generateAccessToken(user);
            return new UserLoginResponse(user.getUsername(), token);
        }
    }
}
