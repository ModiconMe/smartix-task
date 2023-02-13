package io.modicon.smartixtask.domain.repository;

import io.modicon.smartixtask.domain.model.PaymentEntity;
import io.modicon.smartixtask.domain.model.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
    List<PaymentEntity> findByPayee(UserEntity payee, Pageable pageable);
}
