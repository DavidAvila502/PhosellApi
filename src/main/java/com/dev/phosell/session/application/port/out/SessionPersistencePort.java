package com.dev.phosell.session.application.port.out;

import com.dev.phosell.session.domain.model.Session;

import java.util.List;
import java.util.UUID;

public interface SessionPersistencePort {
    Session save(Session session);
    List<Session> findAll();
    List<Session> findByClientId(UUID id);
    List<Session> findByPhotographerId(UUID id);
    List<Session> findBySessionPackageId(UUID id);
}
