package com.dev.phosell.session.application.service;

import com.dev.phosell.authentication.infrastructure.security.CustomUserDetails;
import com.dev.phosell.session.application.dto.SessionStatusChangeDto;
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
public class ChangeSessionStatusService {

    private final SessionStatusChangeValidator sessionStatusChangeValidator;
    private final SessionPersistencePort sessionPersistencePort;
    private final SessionAuthenticityValidator sessionAuthenticityValidator;

    public ChangeSessionStatusService(
            SessionStatusChangeValidator sessionStatusChangeValidator,
            SessionPersistencePort sessionPersistencePort,
            SessionAuthenticityValidator sessionAuthenticityValidator
    )
    {
        this.sessionStatusChangeValidator = sessionStatusChangeValidator;
        this.sessionPersistencePort = sessionPersistencePort;
        this.sessionAuthenticityValidator = sessionAuthenticityValidator;
    }

    public void ChangeStatus(UUID id,SessionStatusChangeDto sessionStatusDto){

        Session session = sessionPersistencePort.findById(id)
                .orElseThrow(() -> new SessionNotFoundException("id", id.toString()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User authenticatedUser = userDetails.getUser();

        SessionStatus newStatusEnum = SessionStatus.fromString(sessionStatusDto.getNewSessionStatus());

        sessionStatusChangeValidator
                .validateStatusAndRolePermissions(
                        session.getSessionStatus(),
                        newStatusEnum,
                        authenticatedUser.getRole()
                );

        if (authenticatedUser.getRole() == Role.PHOTOGRAPHER) {
            sessionAuthenticityValidator.validatePhotographerAssignment(session, authenticatedUser);
        } else {
            sessionAuthenticityValidator.validateOwnerShip(session, authenticatedUser);
        }

        session.setSessionStatus(newStatusEnum);

        sessionPersistencePort.save(session);
    }
}
