package com.dev.phosell.session.infrastructure.persistence.jpa.repository;

import com.dev.phosell.session.infrastructure.persistence.jpa.entity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SessionJpaRepository extends JpaRepository<SessionEntity, UUID>,
        JpaSpecificationExecutor<SessionEntity> {

    List<SessionEntity> findByClientId(UUID id);
    List<SessionEntity> findByPhotographerId(UUID id);
    List<SessionEntity> findBySessionPackageId(UUID id);

    @Query("""
            SELECT s FROM SessionEntity s
            WHERE s.sessionDate = :date
            AND s.sessionStatus
            IN (:statuses)
            """)
    List<SessionEntity> findByDateAndStatusIn(
            @Param("date") LocalDate date ,@Param("statuses") List<String> statuses);

    @Query("""
            SELECT s FROM SessionEntity s
            WHERE s.sessionDate = :date
            AND s.sessionStatus
            NOT IN (:statuses)
            """)
    List<SessionEntity> findByDateAndStatusNotIn(
            @Param("date") LocalDate date, @Param("statuses") List<String> statuses);

    @Modifying
    @Query(value =
            """
            UPDATE sessions
            SET photographer_id =
            CASE id
            WHEN :sessionAId THEN :photographerBId
            WHEN :sessionBId THEN :photographerAId
            END WHERE id IN (:sessionAId, :sessionBId)
            """,
            nativeQuery = true)
    void swapPhotographers(
            @Param("sessionAId")
            UUID sessionAId,
            @Param("sessionBId")
            UUID sessionBId,
            @Param("photographerAId")
            UUID photographerAId,
            @Param("photographerBId")
            UUID photographerBId
    );
}
