package com.dev.phosell.session.infrastructure.persistence.jpa.repository;

import com.dev.phosell.session.infrastructure.persistence.jpa.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SessionJpaRepository extends JpaRepository<SessionEntity, UUID> {
    List<SessionEntity> findByClientId(UUID id);
    List<SessionEntity> findByPhotographerId(UUID id);
    List<SessionEntity> findBySessionPackageId(UUID id);

    @Query("""
            SELECT s FROM SessionEntity s
            WHERE s.sessionDate = :date
            AND s.sessionStatus
            NOT IN (:busyStatuses)
            """)
    List<SessionEntity> findByDateAndStatusNotIn(@Param("date") LocalDate date, @Param("busyStatuses") List<String> statuses);
}
