package com.dev.phosell.sessionpackage.infrastructure.persistence.jpa.repository;

import com.dev.phosell.sessionpackage.infrastructure.persistence.jpa.entity.SessionPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionPackageJpaRepository extends JpaRepository<SessionPackageEntity, UUID> {
}
