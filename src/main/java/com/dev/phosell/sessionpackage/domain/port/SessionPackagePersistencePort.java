package com.dev.phosell.sessionpackage.domain.port;

import com.dev.phosell.sessionpackage.domain.model.SessionPackage;

import java.util.List;

public interface SessionPackagePersistencePort {
    List<SessionPackage> findAll();
}
