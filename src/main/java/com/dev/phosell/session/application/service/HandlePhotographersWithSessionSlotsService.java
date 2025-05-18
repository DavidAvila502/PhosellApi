package com.dev.phosell.session.application.service;

import com.dev.phosell.session.application.port.out.SessionPersistencePort;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.user.application.port.out.FindPhotographersByIsInServicePort;
import com.dev.phosell.user.domain.model.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Getter
@Service
public class HandlePhotographersWithSessionSlotsService {

    @Value("${session.duration}")
    private int sessionDuration;

    private final GenerateSessionSlots generateSessionSlots;
    private final SessionPersistencePort sessionPersistencePort;
    private final FindPhotographersByIsInServicePort findPhotographersByIsInServicePort;

    public List<LocalTime> allSlots = new ArrayList<>();
    public List<Session> busySessions = new ArrayList<>();
    public List<User> photographersInService = new ArrayList<>();
    public Map<UUID, Set<LocalTime>> photographersBlockedSlots = new HashMap<>();
    public List<LocalTime> availableSlots = new ArrayList<>();

    // constructor
    public HandlePhotographersWithSessionSlotsService(
            GenerateSessionSlots generateSessionSlots,
            SessionPersistencePort sessionPersistencePort,
            FindPhotographersByIsInServicePort findPhotographersByIsInServicePort
    ){
        this.generateSessionSlots = generateSessionSlots;
        this.sessionPersistencePort = sessionPersistencePort;
        this.findPhotographersByIsInServicePort = findPhotographersByIsInServicePort;
    }

    public void loadAllSlots(LocalDate date) {
        this.allSlots = generateSessionSlots.generateSlots(date);
    }

    public void loadBusySessions (LocalDate date, List<String> freeStatuses){
        this.busySessions = sessionPersistencePort.findByDateAndStatusNotIn(date,freeStatuses);
    }

    public void loadPhotographersInService(){
        this.photographersInService = findPhotographersByIsInServicePort.findPhotographersByIsInService(true);
    }

    public void calculatePhotographersBlockedSlots(){

        //clean previous data
        this.photographersBlockedSlots.clear();

        //Calculate
        for(User photographer: photographersInService ){
            Set<LocalTime> blocked = new HashSet<>();

            List<Session> sessionsOfCurrentPhotographer = busySessions.stream()
                    .filter(s ->
                            s.getPhotographer().getId().equals(photographer.getId())).toList();

            for (Session currentSession : sessionsOfCurrentPhotographer){
                blocked.add(currentSession.getSessionTime());
                blocked.add(currentSession.getSessionTime().plusMinutes(sessionDuration));
            }

            this.photographersBlockedSlots.put(photographer.getId(),blocked);
        }

    }

    public void calculateAllAvailableSlots(){
        //clear previous data
        this.availableSlots.clear();

        // previous value required
        this.calculatePhotographersBlockedSlots();

        // Calculate the available slots
        for(LocalTime currentSlot : allSlots){

            List<User> freePhotographersInSlot = new ArrayList<>();

            freePhotographersInSlot =  photographersInService.stream().filter(p ->!
                    photographersBlockedSlots.getOrDefault(p.getId(),Collections.emptySet()).contains(currentSlot)).toList();

            if(!freePhotographersInSlot.isEmpty()){
                this.availableSlots.add(currentSlot);
            }

        }
    }

    public List<User> getAvailablePhotographersAtTime(LocalTime time){

        // previous value required
        this.calculatePhotographersBlockedSlots();

        List<User> photographers = new ArrayList<>();

        for (User currentPhotographer : photographersInService){

            if(!photographersBlockedSlots.getOrDefault(currentPhotographer.getId(),Collections.emptySet()).contains(time)){
                photographers.add(currentPhotographer);
            }
        }
        return  photographers;

    }



}
