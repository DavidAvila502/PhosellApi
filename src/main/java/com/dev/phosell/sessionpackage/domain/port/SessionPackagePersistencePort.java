package com.dev.phosell.sessionpackage.domain.port;

import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionPackagePersistencePort {
    List<SessionPackage> findAll();
    Optional<SessionPackage> findById(UUID id);
}
