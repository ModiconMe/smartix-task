package io.modicon.smartixtask.domain.repository;

import io.modicon.smartixtask.domain.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
