package com.dev.phosell.session.application.service;
import com.dev.phosell.session.application.dto.SessionResponseDto;
import com.dev.phosell.session.application.mapper.SessionDtoMapper;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllSessionsService {
    private  final SessionPersistencePort sessionPersistencePort;
    private final SessionDtoMapper sessionDtoMapper;

    public FindAllSessionsService(SessionPersistencePort sessionPersistencePort, SessionDtoMapper sessionDtoMapper){
        this.sessionPersistencePort = sessionPersistencePort;
        this.sessionDtoMapper = sessionDtoMapper;
    }
    public List<SessionResponseDto> findAll(){
        return sessionPersistencePort.findAll().stream()
                .map(s -> sessionDtoMapper.toSessionResponseDto(s)).toList();
    }
}
