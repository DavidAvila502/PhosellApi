package com.dev.phosell.session.infrastructure.persistence.jpa.repository;

import com.dev.phosell.session.infrastructure.persistence.jpa.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SessionJpaRepository extends JpaRepository<SessionEntity, UUID> {
    List<SessionEntity> findByClientId(UUID id);
    List<SessionEntity> findByPhotographerId(UUID id);
    List<SessionEntity> findBySessionPackageId(UUID id);
}
