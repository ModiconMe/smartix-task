package io.modicon.smartixtask.web.controller;

import io.modicon.smartixtask.application.service.PaymentService;
import io.modicon.smartixtask.application.service.SecurityContextHolderService;
import io.modicon.smartixtask.web.dto.PaymentRequest;
import io.modicon.smartixtask.web.dto.PaymentResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public interface PaymentController {

    String BASE_URL_V1 = "/api/v1/payments";

    @SecurityRequirement(name = "basicAuth")
    @PostMapping
    PaymentResponse pay(PaymentRequest request);

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
    }

}
