package com.dev.phosell.session.application.service;
import com.dev.phosell.session.application.dto.SessionResponseDto;
import com.dev.phosell.session.application.mapper.SessionToDtoMapper;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllSessionsService {
    private  final SessionPersistencePort sessionPersistencePort;
    private final SessionToDtoMapper sessionToDtoMapper;

    public FindAllSessionsService(SessionPersistencePort sessionPersistencePort, SessionToDtoMapper sessionToDtoMapper){
        this.sessionPersistencePort = sessionPersistencePort;
        this.sessionToDtoMapper =sessionToDtoMapper;
    }
    public List<SessionResponseDto> findAll(){
        return sessionPersistencePort.findAll().stream()
                .map(s -> sessionToDtoMapper.toSessionResponseDto(s)).toList();
    }
}
