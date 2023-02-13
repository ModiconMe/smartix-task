package io.modicon.smartixtask.web.controller;

import io.modicon.smartixtask.application.service.UserAccessService;
import io.modicon.smartixtask.application.service.UserManagementService;
import io.modicon.smartixtask.web.dto.UserLoginResponse;
import io.modicon.smartixtask.web.dto.UserRegisterRequest;
import io.modicon.smartixtask.web.dto.UserRegisterResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public interface UserOperation {

    String BASE_URL_V1 = "/api/v1/users";

    @PostMapping("/register")
    UserRegisterResponse register(@Valid @RequestBody UserRegisterRequest request);

    @PostMapping("/login")
    UserLoginResponse login();

    @RequiredArgsConstructor
    @RestController
    @RequestMapping(BASE_URL_V1)
    class UserController implements UserOperation {

        private final UserManagementService userManagementService;
        private final UserAccessService userAccessService;

        @Override
        public UserRegisterResponse register(UserRegisterRequest request) {
            return userManagementService.register(request);
        }

        @SecurityRequirement(name = "basicAuth")
        @Override
        public UserLoginResponse login() {
            return userAccessService.login();
        }
    }
}
