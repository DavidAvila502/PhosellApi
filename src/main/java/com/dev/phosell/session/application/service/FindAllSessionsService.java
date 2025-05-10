package com.dev.phosell.session.application.service;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.infrastructure.adapter.out.SessionJpaAdapter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllSessionsService {
    private  final SessionJpaAdapter sessionJpaAdapter;

    public FindAllSessionsService(SessionJpaAdapter sessionJpaAdapter){
        this.sessionJpaAdapter = sessionJpaAdapter;
    }

    public List<Session> findAll(){
        return sessionJpaAdapter.findAll();
    }



}
