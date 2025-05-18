package com.dev.phosell.session.application.service;

import com.dev.phosell.session.application.port.out.SessionPersistencePort;
import com.dev.phosell.session.domain.exception.SessionAlreadyTakenException;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.session.infrastructure.dto.SessionInsertDto;
import com.dev.phosell.sessionpackage.application.port.out.FindSessionPackageByIdPort;
import com.dev.phosell.sessionpackage.domain.exception.SessionPackageNotFoundExcaption;
import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
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


    public  RegisterSessionService(
            SessionPersistencePort sessionPersistencePort,
            FindUserByIdPort findUserByIdPort,
            FindSessionPackageByIdPort findSessionPackageByIdPort,
            IsSessionStillAvailableService isSessionStillAvailableService,
            HandlePhotographersWithSessionSlotsService handlePhotographersWithSessionSlotsService
    ){
        this.sessionPersistencePort = sessionPersistencePort;
        this.findSessionPackageByIdPort =findSessionPackageByIdPort;
        this.findUserByIdPort = findUserByIdPort;
        this.isSessionStillAvailableService = isSessionStillAvailableService;
        this.handlePhotographersWithSessionSlotsService = handlePhotographersWithSessionSlotsService;
    }

    public Session RegisterSession(SessionInsertDto sessionInsert){

        //get the client
        Optional<User> client = findUserByIdPort.findById(sessionInsert.getClientId());

        // if the client doesn't exist throw an exception
        if(client.isEmpty()){
            throw new  UserNotFoundException("id",sessionInsert.getClientId().toString());
        }

        // if the client has not the client role, then throw an exception
        if(!client.get().getRole().equals(Role.CLIENT)){
            throw new  UserNotFoundException("role",Role.CLIENT.toString());
        }

        // get the sessionPackage
        Optional<SessionPackage> sessionPackage =
                findSessionPackageByIdPort.findById(sessionInsert.getSessionPackageId());

        // if the package doesn't exist then throw an error
        if(sessionPackage.isEmpty()){
            throw new SessionPackageNotFoundExcaption("id",sessionInsert.getSessionPackageId().toString());
        }

        // Validate the date and time for this session
        if(!isSessionStillAvailableService.IsSessionStillAvailable(
                sessionInsert.getSessionTime(),
                sessionInsert.getSessionDate())
        ){
            throw new SessionAlreadyTakenException();
        }

        // List of free statuses
        List<String> freeStatuses = List.of(
                SessionStatus.COMPLETED.toString(),
                SessionStatus.CANCELLED_BY_ADMIN.toString(),
                SessionStatus.CANCELLED_BY_CLIENT.toString()
        );


        handlePhotographersWithSessionSlotsService.loadAllSlots(sessionInsert.getSessionDate());
        handlePhotographersWithSessionSlotsService.loadPhotographersInService();
        handlePhotographersWithSessionSlotsService.loadBusySessions(sessionInsert.getSessionDate(),freeStatuses);

        List<User> freePhotographers =
                handlePhotographersWithSessionSlotsService.getAvailablePhotographersAtTime(sessionInsert.getSessionTime());

        if (freePhotographers.isEmpty()){
            throw new SessionAlreadyTakenException();

        }

        // select a free random photographer
        int n = (int)(Math.random() * (freePhotographers.size() -1));


        // create a session object
        Session session = new Session();
        session.setClient(client.get());
        session.setPhotographer(freePhotographers.get(n));
        session.setSessionPackage(sessionPackage.get());
        session.setSessionDate(sessionInsert.getSessionDate());
        session.setSessionTime(sessionInsert.getSessionTime());
        session.setLocation(sessionInsert.getLocation());
        session.setSessionStatus(SessionStatus.REQUESTED);


        return sessionPersistencePort.save(session);
    }



}
