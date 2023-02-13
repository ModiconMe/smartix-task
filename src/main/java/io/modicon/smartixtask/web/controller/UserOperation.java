package io.modicon.smartixtask.web.controller;

import io.modicon.smartixtask.web.dto.UserLoginRequest;
import io.modicon.smartixtask.web.dto.UserLoginResponse;
import io.modicon.smartixtask.web.dto.UserRegisterRequest;
import io.modicon.smartixtask.web.dto.UserRegisterResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public interface UserOperation {

    String BASE_URL_V1 = "/api/v1/users";

    @PostMapping("/register")
    UserRegisterResponse register(@Valid @RequestBody UserRegisterRequest request);

    @PostMapping("/login")
    UserLoginResponse login(@RequestBody UserLoginRequest request);

    @RestController
    @RequestMapping(BASE_URL_V1)
    class UserController implements UserOperation {
        @Override
        public UserRegisterResponse register(UserRegisterRequest request) {
            return null;
        }

        @Override
        public UserLoginResponse login(UserLoginRequest request) {
            return null;
        }
    }
}
