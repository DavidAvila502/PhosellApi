package com.dev.phosell.session.application.service;

import com.dev.phosell.session.application.dto.SessionCancelDto;
import com.dev.phosell.session.application.dto.SessionResponseDto;
import com.dev.phosell.session.application.mapper.SessionDtoMapper;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.session.domain.validator.SessionAuthenticityValidator;
import com.dev.phosell.session.domain.validator.SessionStatusChangeValidator;
import com.dev.phosell.session.infrastructure.exception.SessionNotFoundException;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CancelSessionService {
    private final SessionPersistencePort sessionPersistencePort;
    private final SessionStatusChangeValidator sessionStatusChangeValidator;
    private final SessionAuthenticityValidator sessionAuthenticityValidator;
    private final SessionDtoMapper sessionDtoMapper;

    public CancelSessionService(
            SessionPersistencePort sessionPersistencePort,
            SessionStatusChangeValidator sessionStatusChangeValidator,
            SessionAuthenticityValidator sessionAuthenticityValidator,
            SessionDtoMapper sessionDtoMapper
    )
    {
        this.sessionPersistencePort = sessionPersistencePort;
        this.sessionStatusChangeValidator = sessionStatusChangeValidator;
        this.sessionAuthenticityValidator = sessionAuthenticityValidator;
        this.sessionDtoMapper = sessionDtoMapper;
    }

    public SessionResponseDto cancel(UUID id, SessionCancelDto sessionCancelDto, User authenticatedUser)
    {
        Session session = sessionPersistencePort.findById(id)
                .orElseThrow(() -> new SessionNotFoundException("id",id.toString()));

        SessionStatus newSessionStatus =
                authenticatedUser.getRole() == Role.ADMIN ?
                        SessionStatus.CANCELLED_BY_ADMIN : SessionStatus.CANCELLED_BY_CLIENT;

        sessionStatusChangeValidator
                .validateStatusAndRolePermissions(
                        session.getSessionStatus(),
                        newSessionStatus,
                        authenticatedUser.getRole());

        sessionAuthenticityValidator.validateOwnerShip(session,authenticatedUser);


        session.cancel(authenticatedUser,sessionCancelDto.getCancelReason());

       Session cancelledSession = sessionPersistencePort.save(session);

        return sessionDtoMapper.toSessionResponseDto(cancelledSession);
    }
}
