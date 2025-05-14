package com.dev.phosell.session.application.service;

import com.dev.phosell.session.application.port.out.SessionPersistencePort;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.user.application.port.out.FindPhotographersByIsInServicePort;
import com.dev.phosell.user.domain.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class GetAvailableSessionSlotsService {
    @Value("${session.duration}")
    private int sessionDuration;

    private final GenerateSessionSlots generateSessionSlots;
    private final SessionPersistencePort sessionPersistencePort;
    private final FindPhotographersByIsInServicePort findPhotographersByIsInServicePort;

    public GetAvailableSessionSlotsService(
            GenerateSessionSlots generateSessionSlots,
            SessionPersistencePort sessionPersistencePort,
            FindPhotographersByIsInServicePort findPhotographersByIsInServicePort
    ){
        this.generateSessionSlots = generateSessionSlots;
        this.sessionPersistencePort = sessionPersistencePort;
        this.findPhotographersByIsInServicePort = findPhotographersByIsInServicePort;
    }

    public List<LocalTime> getAvailableSlots(LocalDate date)
    {
        // get all the slots available for the date
        List<LocalTime> allSlots = generateSessionSlots.generateSlots(date);

        // List of free statuses
        List<String> freeStatuses = List.of(
                SessionStatus.COMPLETED.toString(),
                SessionStatus.CANCELLED_BY_ADMIN.toString(),
                SessionStatus.CANCELLED_BY_CLIENT.toString()
        );

        // get the sessions with busy statuses
        List<Session> busySessions = sessionPersistencePort.findByDateAndStatusNotIn(date,freeStatuses);

        //get the photographers in service
        List<User> photographersInService = findPhotographersByIsInServicePort.findPhotographersByIsInService(true);


        // get the blocked slots of each photographer
        Map<UUID, Set<LocalTime>> photographerBlockedSlots = new HashMap<>();
        for(User photographer: photographersInService ){
            Set<LocalTime> blocked = new HashSet<>();

            List<Session> sessionsOfCurrentPhotographer = busySessions.stream()
                    .filter(s ->
                            s.getPhotographer().getId().equals(photographer.getId())).toList();

            for (Session currentSession : sessionsOfCurrentPhotographer){
                blocked.add(currentSession.getSessionTime());
                blocked.add(currentSession.getSessionTime().plusMinutes(sessionDuration));
            }

            photographerBlockedSlots.put(photographer.getId(),blocked);
        }

        // filter all the slots based on the photographers block slots-
        List<LocalTime> filteredSlots = new ArrayList<>();

        for(LocalTime currentSlot : allSlots){

            List<User> freePhotographersInSlot = new ArrayList<>();

          freePhotographersInSlot =  photographersInService.stream().filter(p ->!
                    photographerBlockedSlots.getOrDefault(p.getId(),Collections.emptySet()).contains(currentSlot)).toList();

          if(!freePhotographersInSlot.isEmpty()){
              filteredSlots.add(currentSlot);
          }

        }
        //return the filtered slots
        return filteredSlots;

    }
}
