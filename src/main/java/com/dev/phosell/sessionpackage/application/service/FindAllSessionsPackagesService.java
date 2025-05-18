package com.dev.phosell.sessionpackage.application.service;

import com.dev.phosell.sessionpackage.application.port.out.SessionPackagePersistencePort;
import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllSessionsPackagesService {

    private final SessionPackagePersistencePort sessionPackagePersistencePort;

    public FindAllSessionsPackagesService(SessionPackagePersistencePort sessionPackagePersistencePort){
        this.sessionPackagePersistencePort = sessionPackagePersistencePort;
    }

    public List<SessionPackage> findAll(){
        return sessionPackagePersistencePort.findAll();
    }
}
