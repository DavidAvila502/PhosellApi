package com.dev.phosell.session.application.service;

import com.dev.phosell.authentication.infrastructure.security.CustomUserDetails;
import com.dev.phosell.session.application.dto.SessionCancelDto;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.session.domain.validator.SessionAuthenticityValidator;
import com.dev.phosell.session.domain.validator.SessionStatusChangeValidator;
import com.dev.phosell.session.infrastructure.exception.SessionNotFoundException;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CancelSessionService {
    private final SessionPersistencePort sessionPersistencePort;
    private final SessionStatusChangeValidator sessionStatusChangeValidator;
    private final SessionAuthenticityValidator sessionAuthenticityValidator;

    public CancelSessionService(
            SessionPersistencePort sessionPersistencePort,
            SessionStatusChangeValidator sessionStatusChangeValidator,
            SessionAuthenticityValidator sessionAuthenticityValidator
    )
    {
        this.sessionPersistencePort = sessionPersistencePort;
        this.sessionStatusChangeValidator = sessionStatusChangeValidator;
        this.sessionAuthenticityValidator = sessionAuthenticityValidator;
    }

    public void cancel(UUID id, SessionCancelDto sessionCancelDto)
    {
        Session session = sessionPersistencePort.findById(id)
                .orElseThrow(() -> new SessionNotFoundException("id",id.toString()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User authenticatedUser = userDetails.getUser();

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

        sessionPersistencePort.save(session);


    }
}
