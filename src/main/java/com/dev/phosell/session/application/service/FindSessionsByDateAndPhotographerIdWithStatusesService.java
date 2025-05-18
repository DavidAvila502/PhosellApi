package com.dev.phosell.session.application.service;

import com.dev.phosell.session.application.port.out.SessionPersistencePort;
import com.dev.phosell.session.domain.model.Session;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class FindSessionsByDateAndPhotographerIdWithStatusesService {

    private final SessionPersistencePort sessionPersistencePort;

    public FindSessionsByDateAndPhotographerIdWithStatusesService(
            SessionPersistencePort sessionPersistencePort
    ){
        this.sessionPersistencePort = sessionPersistencePort;
    }
    public List<Session> findSession(UUID photographerId, LocalDate date, List<String> statuses){
        return sessionPersistencePort.findBySessionDateAndPhotographerIdWithStatuses(date,photographerId,statuses);
    }
}
