package com.dev.phosell.session.application.service;

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
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.domain.port.UserPersistencePort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegisterSessionService {

    private final SessionPersistencePort sessionPersistencePort;
    private final SessionSlotsAvailabilityCalculator sessionSlotsAvailabilityCalculator;
    private final SessionDtoMapper sessionDtoMapper;
    private final RegisterSessionValidator registerSessionValidator;
    private final SessionBookingPolicyValidator sessionBookingPolicyValidator;
    private final ChooseRandomPhotographer chooseRandomPhotographer;
    private final UserPersistencePort userPersistencePort;

    public  RegisterSessionService(
            SessionPersistencePort sessionPersistencePort,
            SessionSlotsAvailabilityCalculator sessionSlotsAvailabilityCalculator,
            SessionDtoMapper sessionDtoMapper,
            RegisterSessionValidator registerSessionValidator,
            SessionBookingPolicyValidator sessionBookingPolicyValidator,
            ChooseRandomPhotographer chooseRandomPhotographer,
            UserPersistencePort userPersistencePort
    ){
        this.sessionPersistencePort = sessionPersistencePort;
        this.sessionSlotsAvailabilityCalculator = sessionSlotsAvailabilityCalculator;
        this.sessionDtoMapper = sessionDtoMapper;
        this.registerSessionValidator = registerSessionValidator;
        this.sessionBookingPolicyValidator = sessionBookingPolicyValidator;
        this.chooseRandomPhotographer = chooseRandomPhotographer;
        this.userPersistencePort = userPersistencePort;
    }

    public SessionResponseDto RegisterSession(SessionInsertDto sessionInsert){

        User client = registerSessionValidator.validateClient(sessionInsert.getClientId());

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
                userPersistencePort.findPhotographersByIsInService(true);

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
