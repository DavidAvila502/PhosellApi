package com.dev.phosell.session.application.service;

import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.session.domain.service.GenerateSessionSlots;
import com.dev.phosell.session.domain.service.SessionSlotsAvailabilityCalculator;
import com.dev.phosell.user.application.port.out.FindPhotographersByIsInServicePort;
import com.dev.phosell.user.domain.model.User;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class GetAvailableSessionSlotsService {
    private final SessionSlotsAvailabilityCalculator sessionSlotsAvailabilityCalculator;
    private final GenerateSessionSlots generateSessionSlots;
    private final FindPhotographersByIsInServicePort findPhotographersByIsInServicePort;
    private final SessionPersistencePort sessionPersistencePort;

    public GetAvailableSessionSlotsService(
            SessionSlotsAvailabilityCalculator sessionSlotsAvailabilityCalculator,
            GenerateSessionSlots generateSessionSlots,
            FindPhotographersByIsInServicePort findPhotographersByIsInServicePort,
            SessionPersistencePort sessionPersistencePort
    ){
        this.sessionSlotsAvailabilityCalculator = sessionSlotsAvailabilityCalculator;
        this.generateSessionSlots = generateSessionSlots;
        this.findPhotographersByIsInServicePort = findPhotographersByIsInServicePort;
        this.sessionPersistencePort = sessionPersistencePort;
    }

    public List<LocalTime> getAvailableSlots(LocalDate date)
    {

        // List of free statuses
        List<String> freeStatuses = List.of(
                SessionStatus.COMPLETED.toString(),
                SessionStatus.CANCELLED_BY_ADMIN.toString(),
                SessionStatus.CANCELLED_BY_CLIENT.toString()
        );

        List<User> photographersInService = findPhotographersByIsInServicePort.findPhotographersByIsInService(true);

        List<Session> busySessions = sessionPersistencePort.findByDateAndStatusNotIn(date, freeStatuses);

        List<LocalTime> allPossibleSlots = generateSessionSlots.generateSlots(date);

        List<LocalTime> availableSlots = sessionSlotsAvailabilityCalculator
                .calculateAvailableSlots(busySessions,photographersInService,allPossibleSlots);


        return  availableSlots;

    }
}
