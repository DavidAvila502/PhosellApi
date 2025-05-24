package com.dev.phosell.session.application.service;

import com.dev.phosell.authentication.infrastructure.security.CustomUserDetails;
import com.dev.phosell.session.application.dto.SessionResponseDto;
import com.dev.phosell.session.application.mapper.SessionDtoMapper;
import com.dev.phosell.session.application.validator.RegisterSessionValidator;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.session.domain.service.ChooseRandomPhotographer;
import com.dev.phosell.session.domain.service.SessionSlotsAvailabilityCalculator;
import com.dev.phosell.session.application.dto.SessionInsertDto;
import com.dev.phosell.session.domain.validator.SessionBookingPolicyValidator;
import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.port.FindPhotographersByIsInServicePort;
import com.dev.phosell.user.domain.model.User;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegisterSessionService {

    private final SessionPersistencePort sessionPersistencePort;

    private final SessionSlotsAvailabilityCalculator sessionSlotsAvailabilityCalculator;
    private final FindPhotographersByIsInServicePort findPhotographersByIsInServicePort;
    private final SessionDtoMapper sessionDtoMapper;
    private final RegisterSessionValidator registerSessionValidator;
    private final SessionBookingPolicyValidator sessionBookingPolicyValidator;
    private final ChooseRandomPhotographer chooseRandomPhotographer;

    public  RegisterSessionService(
            SessionPersistencePort sessionPersistencePort,
            SessionSlotsAvailabilityCalculator sessionSlotsAvailabilityCalculator,
            FindPhotographersByIsInServicePort findPhotographersByIsInServicePort,
            SessionDtoMapper sessionDtoMapper,
            RegisterSessionValidator registerSessionValidator,
            SessionBookingPolicyValidator sessionBookingPolicyValidator,
            ChooseRandomPhotographer chooseRandomPhotographer
    ){
        this.sessionPersistencePort = sessionPersistencePort;

        this.sessionSlotsAvailabilityCalculator = sessionSlotsAvailabilityCalculator;
        this.findPhotographersByIsInServicePort = findPhotographersByIsInServicePort;
        this.sessionDtoMapper = sessionDtoMapper;
        this.registerSessionValidator = registerSessionValidator;
        this.sessionBookingPolicyValidator = sessionBookingPolicyValidator;
        this.chooseRandomPhotographer = chooseRandomPhotographer;
    }

    public SessionResponseDto RegisterSession(SessionInsertDto sessionInsert){

        User client = registerSessionValidator.validateClient(sessionInsert.getClientId());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User authenticatedUser = userDetails.getUser();

        if(!authenticatedUser.getId().equals(client.getId()) || authenticatedUser.getRole() != Role.CLIENT)
        {
            throw new AuthorizationDeniedException("Unauthorized action");
        }

        SessionPackage sessionPackage = registerSessionValidator.validateSessionPackage(sessionInsert.getSessionPackageId());

        sessionBookingPolicyValidator.validate(sessionInsert.getSessionDate(),sessionInsert.getSessionTime());

        List<String> freeStatuses = List.of(
                SessionStatus.COMPLETED.toString(),
                SessionStatus.CANCELLED_BY_ADMIN.toString(),
                SessionStatus.CANCELLED_BY_CLIENT.toString()
        );

        List<Session> busySessions =
                sessionPersistencePort.findByDateAndStatusNotIn(sessionInsert.getSessionDate(), freeStatuses);

        List<User> photographersInService =
                findPhotographersByIsInServicePort.findPhotographersByIsInService(true);

        List<User> freePhotographers =
                sessionSlotsAvailabilityCalculator.
                        CalculateAvailablePhotographersAtTime(busySessions,photographersInService,sessionInsert.getSessionTime());

        User photographerChosen = chooseRandomPhotographer.choosePhotographer(freePhotographers);

        Session session = new Session();
        session.setClient(client);
        session.setPhotographer(photographerChosen);
        session.setSessionPackage(sessionPackage);
        session.setSessionDate(sessionInsert.getSessionDate());
        session.setSessionTime(sessionInsert.getSessionTime());
        session.setLocation(sessionInsert.getLocation());
        session.setSessionStatus(SessionStatus.REQUESTED);

        session.validate();

        Session savedSession = sessionPersistencePort.save(session);


        return sessionDtoMapper.toSessionResponseDto(savedSession);
    }
}
