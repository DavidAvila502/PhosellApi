package com.dev.phosell.session.application.service;

import com.dev.phosell.session.application.dto.SwapSessionPhotographersDto;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.session.domain.validator.SessionSwapPhotographerValidator;
import com.dev.phosell.session.infrastructure.exception.SessionNotFoundException;
import com.dev.phosell.user.domain.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SwapSessionPhotographerService {
    private final SessionPersistencePort sessionPersistencePort;
    private final SessionSwapPhotographerValidator sessionSwapPhotographerValidator;

    public SwapSessionPhotographerService(
            SessionPersistencePort sessionPersistencePort,
            SessionSwapPhotographerValidator sessionSwapPhotographerValidator
    )
    {
        this.sessionPersistencePort = sessionPersistencePort;
        this.sessionSwapPhotographerValidator = sessionSwapPhotographerValidator;
    }
    public void swapPhotographers(SwapSessionPhotographersDto swapDto)
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
    }
}
