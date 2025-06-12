package com.dev.phosell.session.application.service;

import com.dev.phosell.session.application.dto.SessionResponseDto;
import com.dev.phosell.session.application.dto.SwapSessionPhotographersDto;
import com.dev.phosell.session.application.mapper.SessionDtoMapper;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.session.domain.service.GetSwapPhotographerSessions;
import com.dev.phosell.session.domain.validator.SessionSwapPhotographerValidator;
import com.dev.phosell.session.infrastructure.exception.SessionNotFoundException;
import com.dev.phosell.user.domain.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SwapSessionPhotographerService {
    private final SessionPersistencePort sessionPersistencePort;
    private final SessionSwapPhotographerValidator sessionSwapPhotographerValidator;
    private final SessionDtoMapper sessionDtoMapper;
    private final GetSwapPhotographerSessions getSwapPhotographerSessions;

    public SwapSessionPhotographerService(
            SessionPersistencePort sessionPersistencePort,
            SessionSwapPhotographerValidator sessionSwapPhotographerValidator,
            SessionDtoMapper sessionDtoMapper,
            GetSwapPhotographerSessions getSwapPhotographerSessions
    )
    {
        this.sessionPersistencePort = sessionPersistencePort;
        this.sessionSwapPhotographerValidator = sessionSwapPhotographerValidator;
        this.sessionDtoMapper = sessionDtoMapper;
        this.getSwapPhotographerSessions = getSwapPhotographerSessions;
    }

    public List<SessionResponseDto> swapPhotographers(SwapSessionPhotographersDto swapDto)
    {
        Session sessionA = sessionPersistencePort.findById(swapDto.getSessionAId())
                .orElseThrow(() -> new SessionNotFoundException("id",swapDto.getSessionAId().toString()));

        Session sessionB  = sessionPersistencePort.findById(swapDto.getSessionBId())
                .orElseThrow(() -> new SessionNotFoundException("id",swapDto.getSessionBId().toString()));

        sessionSwapPhotographerValidator.validate(sessionA,sessionB);

        User photographerA = sessionA.getPhotographer();
        User photographerB = sessionB.getPhotographer();

        sessionPersistencePort.swapPhotographers(
                sessionA.getId(),
                sessionB.getId(),
                photographerA.getId(),
                photographerB.getId()
        );
        // we calculate the swapped values in domain to avoid call the db again.
        List<Session> swappedSessions = getSwapPhotographerSessions.swapPhotographers(sessionA,sessionB);

        return swappedSessions.stream().map(s-> sessionDtoMapper.toSessionResponseDto(s)).toList();
    }
}
