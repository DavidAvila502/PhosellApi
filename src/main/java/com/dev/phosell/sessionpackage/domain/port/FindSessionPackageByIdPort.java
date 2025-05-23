package com.dev.phosell.sessionpackage.domain.port;

import com.dev.phosell.sessionpackage.domain.model.SessionPackage;

import java.util.Optional;
import java.util.UUID;

public interface FindSessionPackageByIdPort {

    Optional<SessionPackage> findById(UUID id);
}
