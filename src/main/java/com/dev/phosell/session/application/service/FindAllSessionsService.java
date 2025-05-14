package com.dev.phosell.session.application.service;
import com.dev.phosell.session.application.port.out.SessionPersistencePort;
import com.dev.phosell.session.domain.model.Session;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllSessionsService {
    private  final SessionPersistencePort sessionPersistencePort;

    public FindAllSessionsService(SessionPersistencePort sessionPersistencePort){
        this.sessionPersistencePort = sessionPersistencePort;
    }
    public List<Session> findAll(){
        return sessionPersistencePort.findAll();
    }
}
