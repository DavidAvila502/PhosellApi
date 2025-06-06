package com.dev.phosell.session.application.service;

import com.dev.phosell.session.application.dto.SessionResponseDto;
import com.dev.phosell.session.application.mapper.SessionDtoMapper;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class GetPhotographerMeSessionService {
    private final SessionPersistencePort sessionPersistencePort;
    private final SessionDtoMapper sessionDtoMapper;

    public  GetPhotographerMeSessionService(
            SessionPersistencePort sessionPersistencePort,
            SessionDtoMapper sessionDtoMapper
    )
    {
        this.sessionPersistencePort = sessionPersistencePort;
        this.sessionDtoMapper = sessionDtoMapper;
    }

    public Page<SessionResponseDto> getSessions(LocalDate date, Pageable pageable,User authenticatedUser)
    {
        Page<Session> sessions = sessionPersistencePort
                .findByFilters(date,null,authenticatedUser.getId(),null,null,pageable);

        return sessions.map(s-> sessionDtoMapper.toSessionResponseDto(s));

    }

}
