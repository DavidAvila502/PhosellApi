package com.dev.phosell.session.application.service;

import com.dev.phosell.session.application.port.out.SessionPersistencePort;
import com.dev.phosell.session.domain.exception.SessionAlreadyTakenException;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.session.infrastructure.dto.SessionInsertDto;
import com.dev.phosell.sessionpackage.application.port.out.FindSessionPackageByIdPort;
import com.dev.phosell.sessionpackage.domain.exception.SessionPackageNotFoundExcaption;
import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import com.dev.phosell.user.application.port.out.FindPhotographersByIsInServicePort;
import com.dev.phosell.user.application.port.out.FindUserByIdPort;
import com.dev.phosell.user.domain.exception.UserNotFoundException;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RegisterSessionService {

    private final SessionPersistencePort sessionPersistencePort;
    private final FindUserByIdPort findUserByIdPort;
    private final FindSessionPackageByIdPort findSessionPackageByIdPort;
    private final IsSessionStillAvailableService isSessionStillAvailableService;
    private final HandlePhotographersWithSessionSlotsService handlePhotographersWithSessionSlotsService;
    private final FindPhotographersByIsInServicePort findPhotographersByIsInServicePort;

    public  RegisterSessionService(
            SessionPersistencePort sessionPersistencePort,
            FindUserByIdPort findUserByIdPort,
            FindSessionPackageByIdPort findSessionPackageByIdPort,
            IsSessionStillAvailableService isSessionStillAvailableService,
            HandlePhotographersWithSessionSlotsService handlePhotographersWithSessionSlotsService,
            FindPhotographersByIsInServicePort findPhotographersByIsInServicePort
    ){
        this.sessionPersistencePort = sessionPersistencePort;
        this.findSessionPackageByIdPort =findSessionPackageByIdPort;
        this.findUserByIdPort = findUserByIdPort;
        this.isSessionStillAvailableService = isSessionStillAvailableService;
        this.handlePhotographersWithSessionSlotsService = handlePhotographersWithSessionSlotsService;
        this.findPhotographersByIsInServicePort = findPhotographersByIsInServicePort;
    }

    public Session RegisterSession(SessionInsertDto sessionInsert){

        Optional<User> client = findUserByIdPort.findById(sessionInsert.getClientId());

        if(client.isEmpty()){
            throw new  UserNotFoundException("id",sessionInsert.getClientId().toString());
        }

        if(!client.get().getRole().equals(Role.CLIENT)){
            throw new  UserNotFoundException("role",Role.CLIENT.toString());
        }

        Optional<SessionPackage> sessionPackage =
                findSessionPackageByIdPort.findById(sessionInsert.getSessionPackageId());

        if(sessionPackage.isEmpty()){
            throw new SessionPackageNotFoundExcaption("id",sessionInsert.getSessionPackageId().toString());
        }

        if(!isSessionStillAvailableService.IsSessionStillAvailable(
                sessionInsert.getSessionTime(),
                sessionInsert.getSessionDate())
        ){
            throw new SessionAlreadyTakenException();
        }

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
                handlePhotographersWithSessionSlotsService.
                        CalculateAvailablePhotographersAtTime(busySessions,photographersInService,sessionInsert.getSessionTime());

        if (freePhotographers.isEmpty()){
            throw new SessionAlreadyTakenException();

        }

        int indexRandomPhotographer = (int)(Math.random() * (freePhotographers.size() -1));

        Session session = new Session();
        session.setClient(client.get());
        session.setPhotographer(freePhotographers.get(indexRandomPhotographer));
        session.setSessionPackage(sessionPackage.get());
        session.setSessionDate(sessionInsert.getSessionDate());
        session.setSessionTime(sessionInsert.getSessionTime());
        session.setLocation(sessionInsert.getLocation());
        session.setSessionStatus(SessionStatus.REQUESTED);


        return sessionPersistencePort.save(session);
    }



}
