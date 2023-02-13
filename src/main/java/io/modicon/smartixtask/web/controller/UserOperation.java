package io.modicon.smartixtask.web.controller;

import io.modicon.smartixtask.application.service.UserService;
import io.modicon.smartixtask.web.dto.UserLoginRequest;
import io.modicon.smartixtask.web.dto.UserLoginResponse;
import io.modicon.smartixtask.web.dto.UserRegisterRequest;
import io.modicon.smartixtask.web.dto.UserRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

public interface UserOperation {

    String BASE_URL_V1 = "/api/v1/users";

    @PostMapping("/register")
    UserRegisterResponse register(@Valid @RequestBody UserRegisterRequest request);

    @PostMapping("/login")
    UserLoginResponse login(@RequestBody UserLoginRequest request);

    @RequiredArgsConstructor
    @RestController
    @RequestMapping(BASE_URL_V1)
    class UserController implements UserOperation {

        private final UserService userService;

        @Override
        public UserRegisterResponse register(UserRegisterRequest request) {
            return userService.register(request);
        }

        @Override
        public UserLoginResponse login(UserLoginRequest request) {
            return null;
        }
    }
}
