package com.dev.phosell.User.infrastructure.persistence.jpa.repositories;

import com.dev.phosell.User.domain.models.Role;
import com.dev.phosell.User.infrastructure.persistence.jpa.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
        Optional<UserEntity> findByEmail(String email);
        List<UserEntity> findByRole(Role role);
}
