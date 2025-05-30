package com.dev.phosell.session.application.service;

import com.dev.phosell.authentication.infrastructure.security.CustomUserDetails;
import com.dev.phosell.session.application.dto.SessionResponseDto;
import com.dev.phosell.session.application.mapper.SessionDtoMapper;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.session.domain.validator.SessionAuthenticityValidator;
import com.dev.phosell.session.infrastructure.exception.SessionNotFoundException;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindSessionByIdService {

    private final SessionPersistencePort sessionPersistencePort;
    private final SessionDtoMapper sessionDtoMapper;
    private final SessionAuthenticityValidator sessionAuthenticityValidator;

    public FindSessionByIdService(
        SessionPersistencePort sessionPersistencePort,
        SessionDtoMapper sessionDtoMapper,
        SessionAuthenticityValidator sessionAuthenticityValidator
    ){
        this.sessionPersistencePort = sessionPersistencePort;
        this.sessionDtoMapper = sessionDtoMapper;
        this.sessionAuthenticityValidator = sessionAuthenticityValidator;
    }


    public SessionResponseDto findSession(UUID sessionId)
    {
        Session session = sessionPersistencePort.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("id",sessionId.toString()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User authenticatedUser = customUserDetails.getUser();

        if(authenticatedUser.getRole() == Role.CLIENT){
            sessionAuthenticityValidator.validateOwnerShip(session,authenticatedUser);
        }else{
            sessionAuthenticityValidator.validatePhotographerAssignment(session,session.getPhotographer());
        }

        return sessionDtoMapper.toSessionResponseDto(session);
    }
}
