package com.dev.phosell.sessionpackage.infrastructure.adapter.out;

import com.dev.phosell.sessionpackage.application.port.out.FindSessionPackageByIdPort;
import com.dev.phosell.sessionpackage.application.port.out.SessionPackagePersistencePort;
import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import com.dev.phosell.sessionpackage.infrastructure.persistence.jpa.entity.SessionPackageEntity;
import com.dev.phosell.sessionpackage.infrastructure.persistence.jpa.repository.SessionPackageJpaRepository;
import com.dev.phosell.sessionpackage.infrastructure.persistence.mapper.SessionPackageMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SessionPackageJpaAdapter implements SessionPackagePersistencePort, FindSessionPackageByIdPort {

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

    @Override
    public Optional<SessionPackage> findById(UUID id) {
        Optional<SessionPackageEntity> sessionPackage = sessionPackageJpaRepository.findById(id);

        return sessionPackage.map(sp -> sessionPackageMapper.toDomain(sp));
    }
}
