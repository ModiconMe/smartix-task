package io.modicon.smartixtask.application.service;

import io.modicon.smartixtask.domain.model.PaymentEntity;
import io.modicon.smartixtask.domain.model.UserEntity;
import io.modicon.smartixtask.domain.repository.PaymentRepository;
import io.modicon.smartixtask.domain.repository.UserRepository;
import io.modicon.smartixtask.web.dto.PaymentRequest;
import io.modicon.smartixtask.web.dto.PaymentResponse;
import io.modicon.smartixtask.web.dto.UserPaymentsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static io.modicon.smartixtask.infrastructure.exception.ApiException.exception;

public interface PaymentService {

    PaymentResponse pay(PaymentRequest request, UserDetails currentUser);

    UserPaymentsResponse getUserPayment(String limit, String page, UserDetails currentUser);

    @Transactional
    @RequiredArgsConstructor
    @Service
    class Base implements PaymentService {

        private final UserRepository userRepository;
        private final PaymentRepository paymentRepository;

        @Override
        public PaymentResponse pay(PaymentRequest request, UserDetails currentUser) {
            String payerTelephone = currentUser.getUsername();
            String payeeTelephone = request.getPayee();
            if (payerTelephone.equals(payeeTelephone))
                throw exception(HttpStatus.BAD_REQUEST, "you cannot pay to yourself");

            UserEntity payer = userRepository.findById(payerTelephone).orElseThrow(() ->
                    exception(HttpStatus.NOT_FOUND, "user with telephone number [%s] not found", payerTelephone));

            UserEntity payee = userRepository.findById(payeeTelephone).orElseThrow(() ->
                    exception(HttpStatus.NOT_FOUND, "user with telephone number [%s] not found", payeeTelephone));

            BigDecimal amount = request.getAmount();
            if (payer.getBalance().compareTo(amount) < 0)
                throw exception(HttpStatus.BAD_REQUEST, "user [%s] has not enough money", payerTelephone);

            PaymentEntity payment = PaymentEntity.builder()
                    .id(UUID.randomUUID().toString())
                    .createdAt(ZonedDateTime.now())
                    .amount(amount)
                    .payer(payer)
                    .payee(payee)
                    .build();

            payer = payer.toBuilder()
                    .balance(payer.getBalance().subtract(amount))
                    .build();
            payee = payee.toBuilder()
                    .balance(payee.getBalance().add(amount))
                    .build();

            userRepository.save(payer);
            userRepository.save(payee);
            paymentRepository.save(payment);

            return new PaymentResponse(UserMapper.mapToDto(payer), UserMapper.mapToDto(payee), payment.getAmount());
        }

        @Transactional(readOnly = true)
        @Override
        public UserPaymentsResponse getUserPayment(String limit, String page, UserDetails currentUser) {
            String telephone = currentUser.getUsername();
            UserEntity user = userRepository.findById(telephone).orElseThrow(() ->
                    exception(HttpStatus.NOT_FOUND, "user with telephone number [%s] not found", telephone));

            List<PaymentEntity> payments = paymentRepository.findByPayee(user, PageRequest.of(Integer.parseInt(page), Integer.parseInt(limit)));

            return new UserPaymentsResponse(payments.stream().map(PaymentMapper::mapToDto).toList());
        }
    }
}
