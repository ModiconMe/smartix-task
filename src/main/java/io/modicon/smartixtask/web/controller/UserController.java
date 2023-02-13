package io.modicon.smartixtask.web.controller;

import io.modicon.smartixtask.application.service.SecurityContextHolderService;
import io.modicon.smartixtask.application.service.UserAccessService;
import io.modicon.smartixtask.application.service.UserManagementService;
import io.modicon.smartixtask.web.dto.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

public interface UserController {

    String BASE_URL_V1 = "/api/v1/users";

    @PostMapping("/register")
    UserRegisterResponse register(@Valid @RequestBody UserRegisterRequest request);

    @SecurityRequirement(name = "basicAuth")
    @PostMapping("/login")
    UserLoginResponse login();

    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/balance")
    UserBalanceResponse getBalance();

    @SecurityRequirement(name = "basicAuth")
    @PostMapping("/update")
    UserUpdateResponse updateUser(@Valid @RequestBody UserUpdateRequest request);

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

        @Override
        public UserLoginResponse login() {
            return userAccessService.login(securityContextHolderService.getCurrentUser());
        }

        @Override
        public UserBalanceResponse getBalance() {
            return userManagementService.getBalance(securityContextHolderService.getCurrentUser());
        }

        @Override
        public UserUpdateResponse updateUser(UserUpdateRequest request) {
            return userManagementService.updateUser(request, securityContextHolderService.getCurrentUser());
        }
    }
}
