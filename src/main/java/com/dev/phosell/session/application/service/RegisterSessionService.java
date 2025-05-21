package com.dev.phosell.session.application.service;

import com.dev.phosell.session.application.dto.SessionResponseDto;
import com.dev.phosell.session.application.mapper.SessionToDtoMapper;
import com.dev.phosell.session.application.validator.RegisterSessionValidator;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.session.application.exception.SessionAlreadyTakenException;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.session.domain.service.SessionSlotsAvailabilityCalculator;
import com.dev.phosell.session.application.dto.SessionInsertDto;
import com.dev.phosell.session.domain.validator.SessionBookingPolicyValidator;
import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import com.dev.phosell.user.application.port.out.FindPhotographersByIsInServicePort;
import com.dev.phosell.user.domain.model.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegisterSessionService {

    private final SessionPersistencePort sessionPersistencePort;

    private final SessionSlotsAvailabilityCalculator sessionSlotsAvailabilityCalculator;
    private final FindPhotographersByIsInServicePort findPhotographersByIsInServicePort;
    private final SessionToDtoMapper sessionToDtoMapper;
    private final RegisterSessionValidator registerSessionValidator;
    private final SessionBookingPolicyValidator sessionBookingPolicyValidator;

    public  RegisterSessionService(
            SessionPersistencePort sessionPersistencePort,
            SessionSlotsAvailabilityCalculator sessionSlotsAvailabilityCalculator,
            FindPhotographersByIsInServicePort findPhotographersByIsInServicePort,
            SessionToDtoMapper sessionToDtoMapper,
            RegisterSessionValidator registerSessionValidator,
            SessionBookingPolicyValidator sessionBookingPolicyValidator
    ){
        this.sessionPersistencePort = sessionPersistencePort;

        this.sessionSlotsAvailabilityCalculator = sessionSlotsAvailabilityCalculator;
        this.findPhotographersByIsInServicePort = findPhotographersByIsInServicePort;
        this.sessionToDtoMapper = sessionToDtoMapper;
        this.registerSessionValidator = registerSessionValidator;
        this.sessionBookingPolicyValidator = sessionBookingPolicyValidator;
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
                findPhotographersByIsInServicePort.findPhotographersByIsInService(true);

        List<User> freePhotographers =
                sessionSlotsAvailabilityCalculator.
                        CalculateAvailablePhotographersAtTime(busySessions,photographersInService,sessionInsert.getSessionTime());

        if (freePhotographers.isEmpty()){
            throw new SessionAlreadyTakenException();

        }

        int indexRandomPhotographer = (int)(Math.random() * (freePhotographers.size() -1));

        Session session = new Session();
        session.setClient(client);
        session.setPhotographer(freePhotographers.get(indexRandomPhotographer));
        session.setSessionPackage(sessionPackage);
        session.setSessionDate(sessionInsert.getSessionDate());
        session.setSessionTime(sessionInsert.getSessionTime());
        session.setLocation(sessionInsert.getLocation());
        session.setSessionStatus(SessionStatus.REQUESTED);

        Session savedSession = sessionPersistencePort.save(session);


        return sessionToDtoMapper.toSessionResponseDto(savedSession);
    }



}
