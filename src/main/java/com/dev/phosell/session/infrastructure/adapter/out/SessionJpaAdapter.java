package com.dev.phosell.session.infrastructure.adapter.out;

import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.infrastructure.persistence.jpa.entity.SessionEntity;
import com.dev.phosell.session.infrastructure.persistence.jpa.repository.SessionJpaRepository;
import com.dev.phosell.session.infrastructure.persistence.jpa.repository.SessionSpecifications;
import com.dev.phosell.session.infrastructure.persistence.mapper.SessionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SessionJpaAdapter implements SessionPersistencePort {

    private final SessionJpaRepository sessionJpaRepository;
    private final SessionMapper sessionMapper;

    public SessionJpaAdapter(
            SessionJpaRepository sessionJpaRepository,
            SessionMapper sessionMapper
    ){
        this.sessionJpaRepository = sessionJpaRepository;
        this.sessionMapper = sessionMapper;
    }

    @Override
    public Optional<Session> findById(UUID id) {
        return sessionJpaRepository.findById(id).map(s -> sessionMapper.toDomain(s));
    }

    @Override
    public Session save(Session session) {

        SessionEntity sessionEntity = sessionMapper.toEntity(session);

        sessionJpaRepository.save(sessionEntity);

        return sessionMapper.toDomain(sessionEntity);
    }

    @Override
    public List<Session> findAll() {
        return sessionJpaRepository.findAll().stream()
                .map(session -> sessionMapper.toDomain(session)).toList();
    }

    @Override
    public List<Session> findByClientId(UUID id) {
        return sessionJpaRepository.findByClientId(id).stream()
                .map(session-> sessionMapper.toDomain(session)).toList();
    }

    @Override
    public List<Session> findByPhotographerId(UUID id) {
        return sessionJpaRepository.findByPhotographerId(id).stream()
                .map(session -> sessionMapper.toDomain(session)).toList();
    }

    @Override
    public List<Session> findBySessionPackageId(UUID id) {
        return sessionJpaRepository.findBySessionPackageId(id).stream()
                .map(session -> sessionMapper.toDomain(session)).toList();
    }

    @Override
    public List<Session> findByDateAndStatusNotIn(LocalDate date,List<String> statuses) {
        return sessionJpaRepository.findByDateAndStatusNotIn(date,statuses).stream()
                .map(s -> sessionMapper.toDomain(s)).toList();
    }

    @Override
    public List<Session> findByDateAndStatusIn(LocalDate date, List<String> statuses) {
        return sessionJpaRepository.findByDateAndStatusIn(date, statuses).stream()
                .map(s -> sessionMapper.toDomain(s)).toList();
    }

    @Override
    public Page<Session> findByFilters(
            LocalDate date,
            LocalTime time,
            UUID photographerId,
            UUID clientId,
            List<SessionStatus> statuses,
            Pageable pageable
    )
    {
        Specification<SessionEntity> sessionSpecifications = Specification
                        .where(SessionSpecifications.byDate(date))
                        .and(SessionSpecifications.byTime(time))
                        .and(SessionSpecifications.byPhotographer(photographerId))
                        .and(SessionSpecifications.byClient(clientId))
                        .and(SessionSpecifications.byStatusIn(statuses));

        return sessionJpaRepository
                .findAll(sessionSpecifications,pageable)
                .map(s->sessionMapper.toDomain(s));
    }

    @Override
    public void swapPhotographers(UUID sessionAId, UUID sessionBId, UUID photographerAId, UUID photographerBId) {
        sessionJpaRepository.swapPhotographers(sessionAId,sessionBId,photographerAId,photographerBId);
    }
}
