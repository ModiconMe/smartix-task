package io.modicon.smartixtask.domain.repository;

import io.modicon.smartixtask.domain.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, String> {
}
