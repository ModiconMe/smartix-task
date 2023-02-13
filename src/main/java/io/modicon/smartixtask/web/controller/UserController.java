package io.modicon.smartixtask.web.controller;

import io.modicon.smartixtask.application.service.SecurityContextHolderService;
import io.modicon.smartixtask.application.service.UserAccessService;
import io.modicon.smartixtask.application.service.UserManagementService;
import io.modicon.smartixtask.web.dto.UserBalanceResponse;
import io.modicon.smartixtask.web.dto.UserLoginResponse;
import io.modicon.smartixtask.web.dto.UserRegisterRequest;
import io.modicon.smartixtask.web.dto.UserRegisterResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

public interface UserController {

    String BASE_URL_V1 = "/api/v1/users";

    @PostMapping("/register")
    UserRegisterResponse register(@Valid @RequestBody UserRegisterRequest request);

    @PostMapping("/login")
    UserLoginResponse login();

    @GetMapping("/balance")
    UserBalanceResponse getBalance();

    @RequiredArgsConstructor
    @RestController
    @RequestMapping(BASE_URL_V1)
    class Base implements UserController {

        private final UserManagementService userManagementService;
        private final UserAccessService userAccessService;
        private final SecurityContextHolderService securityContextHolderService;

        @Override
        public UserRegisterResponse register(UserRegisterRequest request) {
            return userManagementService.register(request);
        }

        @SecurityRequirement(name = "basicAuth")
        @Override
        public UserLoginResponse login() {
            return userAccessService.login(securityContextHolderService.getCurrentUser());
        }

        @SecurityRequirement(name = "basicAuth")
        @Override
        public UserBalanceResponse getBalance() {
            return userManagementService.getBalance(securityContextHolderService.getCurrentUser());
        }
    }
}
