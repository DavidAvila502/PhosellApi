package com.dev.phosell.session.application.service;

import com.dev.phosell.session.domain.exception.photographer.NotAvailablePhotographerException;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.session.domain.service.SessionSlotsAvailabilityCalculator;
import com.dev.phosell.session.infrastructure.exception.SessionNotFoundException;
import com.dev.phosell.user.application.exception.UserNotFoundException;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.domain.port.UserPersistencePort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ReassignPhotographerService {

    private  final SessionPersistencePort sessionPersistencePort;
    private final SessionSlotsAvailabilityCalculator sessionSlotsAvailabilityCalculator;
    private final UserPersistencePort userPersistencePort;

    public ReassignPhotographerService(
            SessionPersistencePort sessionPersistencePort,
            SessionSlotsAvailabilityCalculator sessionSlotsAvailabilityCalculator,
            UserPersistencePort userPersistencePort
    ){
        this.sessionPersistencePort = sessionPersistencePort;
        this.sessionSlotsAvailabilityCalculator = sessionSlotsAvailabilityCalculator;
        this.userPersistencePort = userPersistencePort;
    }

    public void reassign(UUID sessionId, UUID photographerId)
    {
        Session session = sessionPersistencePort.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("id",sessionId.toString()));

        User newPhotographer = userPersistencePort.findById(photographerId)
                .orElseThrow(() -> new UserNotFoundException("id",photographerId.toString()));

        List<String> freeStatuses = SessionStatus.toListString(SessionStatus.getFreeStatuses());

        List<Session> busySessions = sessionPersistencePort
                .findByDateAndStatusNotIn(session.getSessionDate(),freeStatuses);

        List<User> allPhotographersInService = userPersistencePort.findPhotographersByIsInService(true);

        if(allPhotographersInService.isEmpty()){
            throw new NotAvailablePhotographerException();
        }

        List<User> freePhotographers = sessionSlotsAvailabilityCalculator
                .CalculateAvailablePhotographersAtTime(busySessions,allPhotographersInService,session.getSessionTime());

        boolean isNewPhotographerFree = freePhotographers.stream()
                .map(p-> p.getId())
                .anyMatch( currentPhotographerId->currentPhotographerId.equals(newPhotographer.getId()));

        if(!isNewPhotographerFree){
            throw new NotAvailablePhotographerException();
        }

        session.setPhotographer(newPhotographer);

        sessionPersistencePort.save(session);
    }
}
