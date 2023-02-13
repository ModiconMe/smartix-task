package io.modicon.smartixtask.domain.repository;

import io.modicon.smartixtask.domain.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
