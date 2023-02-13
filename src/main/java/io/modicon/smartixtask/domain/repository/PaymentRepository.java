package io.modicon.smartixtask.domain.repository;

import io.modicon.smartixtask.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
