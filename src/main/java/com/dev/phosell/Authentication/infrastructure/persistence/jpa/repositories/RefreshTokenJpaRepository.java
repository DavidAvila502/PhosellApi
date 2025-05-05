package com.dev.phosell.Authentication.infrastructure.persistence.jpa.repositories;

import com.dev.phosell.Authentication.infrastructure.persistence.jpa.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteByToken(String token);

}
