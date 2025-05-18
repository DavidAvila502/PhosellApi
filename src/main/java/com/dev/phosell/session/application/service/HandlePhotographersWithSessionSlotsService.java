package com.dev.phosell.session.application.service;

import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.user.domain.model.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.*;

@Data
@Service
public class HandlePhotographersWithSessionSlotsService {

    @Value("${session.duration}")
    private int sessionDuration;

    // constructor
    public HandlePhotographersWithSessionSlotsService() {}


    /**
     * Calculate all the blocked slots for each photographer in service.
     *
     * @param sessions sessions with busy statuses of a date
     * @param photographers photographers with isInService = true
     * @return A map of photographer with id as key and a set as value with the blocked slots for that photographer
     * */
    public Map<UUID, Set<LocalTime>> calculatePhotographersBlockedSlots(List<Session> sessions, List<User> photographers){

        Map<UUID, Set<LocalTime>> photographersBlockedSlots = new HashMap<>();
        for(User photographer: photographers ){
            Set<LocalTime> blocked = new HashSet<>();

            List<Session> sessionsOfCurrentPhotographer = sessions.stream()
                    .filter(s ->
                            s.getPhotographer().getId().equals(photographer.getId())).toList();

            for (Session currentSession : sessionsOfCurrentPhotographer){
                blocked.add(currentSession.getSessionTime());
                blocked.add(currentSession.getSessionTime().plusMinutes(sessionDuration));
            }

            photographersBlockedSlots.put(photographer.getId(),blocked);
        }

        return photographersBlockedSlots;
    }

    /**
     * Calculate all the still available slots to have a session (left slots).
     *
     * @param sessions sessions with busy statuses of a date
     * @param photographers photographers with isInService = true
     * @param slots all possible slots (raw data)
     * @return a list with the available slots
     * */
    public List<LocalTime> calculateAvailableSlots(List<Session> sessions, List<User> photographers,List<LocalTime> slots){

        // previous values required
        Map<UUID ,Set<LocalTime>> photographersBlockedSlots = this.calculatePhotographersBlockedSlots(sessions,photographers);


        List<LocalTime> availableSlots = new ArrayList<>();
        for(LocalTime currentSlot : slots){

            List<User> freePhotographersInSlot = new ArrayList<>();

            freePhotographersInSlot =  photographers.stream().filter(p ->!
                    photographersBlockedSlots.getOrDefault(p.getId(),Collections.emptySet()).contains(currentSlot)).toList();

            if(!freePhotographersInSlot.isEmpty()){
                availableSlots.add(currentSlot);
            }
        }

        return availableSlots;
    }


    /**
     * Calculate what users are free in a given slot.
     *
     * @param sessions sessions with busy statuses of a date
     * @param photographers photographers with isInService = true
     * @param time the slot or time you want to find an available photographer
     * @return A list with the found users
     * */
    public List<User> CalculateAvailablePhotographersAtTime(List<Session>sessions,List<User>photographers,LocalTime time){

        // previous value required
       Map<UUID,Set< LocalTime>> photographersBlockedSlots = calculatePhotographersBlockedSlots(sessions,photographers);

        List<User> freePhotographers = new ArrayList<>();
        for (User currentPhotographer : photographers){

            if(!photographersBlockedSlots.getOrDefault(currentPhotographer.getId(),Collections.emptySet()).contains(time)){
                freePhotographers.add(currentPhotographer);
            }
        }
        return  freePhotographers;

    }
}
