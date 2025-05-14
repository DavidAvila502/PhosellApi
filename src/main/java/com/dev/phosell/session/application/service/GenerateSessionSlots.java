package com.dev.phosell.session.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GenerateSessionSlots {

    @Value("${sessions.earliest-booking-hour}")
    private int earliestBookingHour;

    @Value("${sessions.latest-booking-hour}")
    private int latestBookingHour;

    @Value("${sessions.earliest-start-working-hour}")
    private int earliestStartWorkingHour;

    @Value("${sessions.latest-start-working-hour}")
    private int latestStartWorkingHour;

    @Value("${sessions.advance-hours}")
    private int advanceHours;

    @Value("${session.duration}")
    private int sessionDuration;

    public List<LocalTime> generateSlots(LocalDate date){

        LocalDateTime now   = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate today   = now.toLocalDate();

        // If the date is before today return an empty list
        if (date.isBefore(today)) {
            return Collections.emptyList();
        }

        return date.equals(today) ? generateForToday(today,now) : generateForTheFuture(date);

    }

    private List<LocalTime> generateForToday(LocalDate today, LocalDateTime now){

        // if the users is trying to request a session in an unallowed hour return empty []
        if(now.isAfter(today.atTime(latestBookingHour,0)) || now.isBefore(today.atTime(earliestBookingHour,0))){
            return Collections.emptyList();
        }

        LocalDateTime minHourToHaveASessionToday = today.atTime(earliestStartWorkingHour,0);

        // next session 3 hours from now
        LocalDateTime candidateSession = now.plusHours(advanceHours);

        // Round the hour to the next if necessary
        candidateSession = roundHourToTheNext(candidateSession);

        if(candidateSession.isBefore(minHourToHaveASessionToday)){
            return Collections.emptyList();
        }

        List<LocalTime> slots = getSlots(
                candidateSession,
                today.atTime(latestStartWorkingHour,0)
        );
        return slots;
    }

    private  List<LocalTime> generateForTheFuture(LocalDate date){
        return getSlots(
                date.atTime(earliestStartWorkingHour,0),
                date.atTime(latestStartWorkingHour,0)
        );
    }

    private LocalDateTime roundHourToTheNext(LocalDateTime dateAndHour){
        int totalMins = dateAndHour.getHour() * 60 + dateAndHour.getMinute();
        int rem       = totalMins % sessionDuration;
        if (rem != 0) {
            dateAndHour = dateAndHour.plusMinutes(sessionDuration - rem);
        }
        return dateAndHour;
    }


    private List<LocalTime> getSlots(
            LocalDateTime dateAndHourToStartGeneration,
            LocalDateTime dateAndHourToEndGeneration
    ){
        // Generate the slots
        List<LocalTime> slots = new ArrayList<>();
        LocalDateTime cursor = dateAndHourToStartGeneration;
        while (cursor.isBefore(dateAndHourToEndGeneration.plusHours(1))) {
            slots.add(cursor.toLocalTime());
            cursor = cursor.plusMinutes(sessionDuration);
        }
        return slots;

    }

}
