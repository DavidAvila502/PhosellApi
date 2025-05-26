package com.dev.phosell.session.application.service;

import com.dev.phosell.authentication.infrastructure.security.CustomUserDetails;
import com.dev.phosell.session.application.dto.CompleteSessionDto;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.session.domain.validator.SessionStatusChangeValidator;
import com.dev.phosell.session.infrastructure.exception.SessionNotFoundException;
import com.dev.phosell.user.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CompleteSessionService {

    private final SessionPersistencePort sessionPersistencePort;
    private final SessionStatusChangeValidator sessionStatusChangeValidator;

    public CompleteSessionService(
            SessionPersistencePort sessionPersistencePort,
            SessionStatusChangeValidator sessionStatusChangeValidator
    )
    {
        this.sessionPersistencePort = sessionPersistencePort;
        this.sessionStatusChangeValidator = sessionStatusChangeValidator;
    }

    public void complete(UUID id, CompleteSessionDto completeSessionDto)
    {
        Session session = sessionPersistencePort.findById(id)
                .orElseThrow(() -> new SessionNotFoundException("id",id.toString()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User authenticatedUser = customUserDetails.getUser();

        sessionStatusChangeValidator
                .validateStatusAndRolePermissions(
                        session.getSessionStatus(),
                        SessionStatus.COMPLETED,
                        authenticatedUser.getRole());

        sessionStatusChangeValidator.validatePhotographerAssignment(session,authenticatedUser);

        session.complete(authenticatedUser,completeSessionDto.getPhotosLink());

        sessionPersistencePort.save(session);

    }
}
