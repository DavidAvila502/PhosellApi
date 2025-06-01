package com.dev.phosell.session.application.service;

import com.dev.phosell.session.application.dto.CompleteSessionDto;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.session.domain.validator.SessionAuthenticityValidator;
import com.dev.phosell.session.domain.validator.SessionStatusChangeValidator;
import com.dev.phosell.session.infrastructure.exception.SessionNotFoundException;
import com.dev.phosell.user.domain.model.User;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CompleteSessionService {

    private final SessionPersistencePort sessionPersistencePort;
    private final SessionStatusChangeValidator sessionStatusChangeValidator;
    private final SessionAuthenticityValidator sessionAuthenticityValidator;

    public CompleteSessionService(
            SessionPersistencePort sessionPersistencePort,
            SessionStatusChangeValidator sessionStatusChangeValidator,
            SessionAuthenticityValidator sessionAuthenticityValidator
    )
    {
        this.sessionPersistencePort = sessionPersistencePort;
        this.sessionStatusChangeValidator = sessionStatusChangeValidator;
        this.sessionAuthenticityValidator = sessionAuthenticityValidator;
    }

    public void complete(UUID id, CompleteSessionDto completeSessionDto,User authenticatedUser)
    {
        Session session = sessionPersistencePort.findById(id)
                .orElseThrow(() -> new SessionNotFoundException("id",id.toString()));

        sessionStatusChangeValidator
                .validateStatusAndRolePermissions(
                        session.getSessionStatus(),
                        SessionStatus.COMPLETED,
                        authenticatedUser.getRole());

        sessionAuthenticityValidator.validatePhotographerAssignment(session,authenticatedUser);

        session.complete(authenticatedUser,completeSessionDto.getPhotosLink());

        sessionPersistencePort.save(session);

    }
}
