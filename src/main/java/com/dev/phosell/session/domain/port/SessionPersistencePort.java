package com.dev.phosell.session.domain.port;

import com.dev.phosell.session.domain.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionPersistencePort {
    Optional<Session> findById(UUID id);
    Session save(Session session);
    List<Session> findAll();
    List<Session> findByClientId(UUID id);
    List<Session> findByPhotographerId(UUID id);
    List<Session> findBySessionPackageId(UUID id);
    List<Session> findByDateAndStatusNotIn(LocalDate date, List<String> statuses);
    List<Session> findBySessionDateAndPhotographerIdWithStatuses(LocalDate date , UUID id,List<String> statuses);
    Page<Session> findByFilters(
            UUID photographerId,UUID clientId, LocalDate date, Pageable pageable);
}
