package com.dev.phosell.sessionpackage.infrastructure.adapter.out;

import com.dev.phosell.sessionpackage.application.port.out.SessionPackagePersistencePort;
import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import com.dev.phosell.sessionpackage.infrastructure.persistence.jpa.repository.SessionPackageJpaRepository;
import com.dev.phosell.sessionpackage.infrastructure.persistence.mapper.SessionPackageMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SessionPackageJpaAdapter implements SessionPackagePersistencePort {

    private final SessionPackageJpaRepository sessionPackageJpaRepository;
    private final SessionPackageMapper sessionPackageMapper;

    public  SessionPackageJpaAdapter(
            SessionPackageJpaRepository sessionPackageJpaRepository,
            SessionPackageMapper sessionPackageMapper
    ){
        this.sessionPackageJpaRepository = sessionPackageJpaRepository;
        this.sessionPackageMapper = sessionPackageMapper;
    }


    @Override
    public List<SessionPackage> findAll() {
        return sessionPackageJpaRepository.findAll().stream()
                .map(session ->
                        sessionPackageMapper.toDomain(session)).toList();
    }
}
