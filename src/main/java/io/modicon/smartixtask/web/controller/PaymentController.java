package io.modicon.smartixtask.web.controller;

import io.modicon.smartixtask.application.service.PaymentService;
import io.modicon.smartixtask.application.service.SecurityContextHolderService;
import io.modicon.smartixtask.web.dto.PaymentRequest;
import io.modicon.smartixtask.web.dto.PaymentResponse;
import io.modicon.smartixtask.web.dto.UserPaymentsResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

public interface PaymentController {

    String BASE_URL_V1 = "/api/v1/payments";

    @SecurityRequirement(name = "basicAuth")
    @PostMapping
    PaymentResponse pay(@Valid @RequestBody PaymentRequest request);

    @SecurityRequirement(name = "basicAuth")
    @GetMapping
    UserPaymentsResponse getPayments(@RequestParam(value = "limit", defaultValue = "10") String limit,
                                     @RequestParam(value = "page", defaultValue = "0") String page);

    @RequiredArgsConstructor
    @RestController
    @RequestMapping(BASE_URL_V1)
    class Base implements PaymentController {

        private final SecurityContextHolderService securityContextHolderService;
        private final PaymentService paymentService;

        @Override
        public PaymentResponse pay(PaymentRequest request) {
            return paymentService.pay(request, securityContextHolderService.getCurrentUser());
        }

        @Override
        public UserPaymentsResponse getPayments(String limit, String page) {
            return paymentService.getUserPayment(limit, page, securityContextHolderService.getCurrentUser());
        }
    }

}
