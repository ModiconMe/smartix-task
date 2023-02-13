package io.modicon.smartixtask.application.service;

import io.modicon.smartixtask.infrastructure.security.CustomUserDetails;
import io.modicon.smartixtask.infrastructure.security.jwt.JwtGeneration;
import io.modicon.smartixtask.web.dto.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

public interface UserAccessService {

    UserLoginResponse login();

    @RequiredArgsConstructor
    @Service
    class Base implements UserAccessService {

        private final JwtGeneration jwtGeneration;

        @Override
        public UserLoginResponse login() {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String password = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

            String token = jwtGeneration.generateAccessToken(new CustomUserDetails(username, password));
            return new UserLoginResponse(username, token);
        }
    }
}
