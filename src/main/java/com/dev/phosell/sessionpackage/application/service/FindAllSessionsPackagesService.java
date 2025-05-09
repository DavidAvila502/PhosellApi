package com.dev.phosell.sessionpackage.application.service;

import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import com.dev.phosell.sessionpackage.infrastructure.adapter.out.SessionPackageJpaAdapter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllSessionsPackagesService {

    private final SessionPackageJpaAdapter sessionPackageJpaAdapter;

    public FindAllSessionsPackagesService(SessionPackageJpaAdapter sessionPackageJpaAdapter){
        this.sessionPackageJpaAdapter = sessionPackageJpaAdapter;
    }

    public List<SessionPackage> findAll(){
        return sessionPackageJpaAdapter.findAll();
    }
}
