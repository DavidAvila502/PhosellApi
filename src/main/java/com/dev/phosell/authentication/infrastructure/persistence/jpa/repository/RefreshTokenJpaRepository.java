package com.dev.phosell.authentication.infrastructure.persistence.jpa.repository;

import com.dev.phosell.authentication.infrastructure.persistence.jpa.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByToken(String token);

}
