package io.modicon.smartixtask.web.controller;

import io.modicon.smartixtask.application.service.SecurityContextHolderService;
import io.modicon.smartixtask.application.service.UserAccessService;
import io.modicon.smartixtask.application.service.UserManagementService;
import io.modicon.smartixtask.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

public interface UserController {

    String BASE_URL_V1 = "/api/v1/users";

    @Operation(summary = "register user by telephone and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user payments",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid data provided.",
                    content = @Content)})
    @PostMapping("/register")
    ResponseEntity<?> register(@Valid @RequestBody UserRegisterRequest request);

    @Operation(summary = "return jwt token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jwt token successfully generated",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "401", description = "Authentication error.",
                    content = @Content) })
    @SecurityRequirement(name = "basicAuth")
    @PostMapping("/login")
    ResponseEntity<?> login();

    @Operation(summary = "get user balance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jwt token successfully generated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserBalanceResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication error.",
                    content = @Content) })
    @SecurityRequirement(name = "basicAuth")
    @GetMapping("/balance")
    UserBalanceResponse getBalance();

    @Operation(summary = "update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jwt token successfully generated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserUpdateResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid data provided.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Authentication error.",
                    content = @Content) })
    @SecurityRequirement(name = "basicAuth")
    @PutMapping("/update")
    UserUpdateResponse updateUser(@Valid @RequestBody UserUpdateRequest request);

    @RequiredArgsConstructor
    @RestController
    @RequestMapping(BASE_URL_V1)
    class Base implements UserController {

        private final UserManagementService userManagementService;
        private final UserAccessService userAccessService;
        private final SecurityContextHolderService securityContextHolderService;

        @Override
        public ResponseEntity<?> register(UserRegisterRequest request) {
            UserRegisterResponse response = userManagementService.register(request);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, response.getToken())
                    .body(response.getUser());
        }

        @Override
        public ResponseEntity<?> login() {
            var response = userAccessService.login(securityContextHolderService.getCurrentUser());
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, response.getToken())
                    .body(response.getUser());
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
