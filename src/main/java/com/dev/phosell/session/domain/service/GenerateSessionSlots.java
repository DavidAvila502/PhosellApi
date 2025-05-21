package com.dev.phosell.session.domain.service;

import com.dev.phosell.session.domain.validator.SlotGenerationValidator;
import com.dev.phosell.session.infrastructure.config.SessionConfig;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenerateSessionSlots {

    private final SessionConfig sessionConfig;
    private final SlotGenerationValidator slotGenerationValidator;

    public GenerateSessionSlots(SessionConfig sessionConfig, SlotGenerationValidator slotGenerationValidator){
        this.sessionConfig = sessionConfig;
        this.slotGenerationValidator = slotGenerationValidator;
    }


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

        // next session 3 hours from now
        LocalDateTime candidateSession = now.plusHours(sessionConfig.getAdvanceHours());

        // Round the hour to the next if necessary
        candidateSession = roundHourToTheNext(candidateSession);

        if(!slotGenerationValidator.validateSlotForToday(candidateSession)){
            return Collections.emptyList();
        };

        List<LocalTime> slots = getSlots(
                candidateSession,
                today.atTime(sessionConfig.getLatestStartWorkingHour(),0)
        );
        return slots;
    }

    private  List<LocalTime> generateForTheFuture(LocalDate date){
        return getSlots(
                date.atTime(sessionConfig.getEarliestStartWorkingHour(),0),
                date.atTime(sessionConfig.getLatestStartWorkingHour(),0)
        );
    }

    private LocalDateTime roundHourToTheNext(LocalDateTime dateAndHour){
        int totalMins = dateAndHour.getHour() * 60 + dateAndHour.getMinute();
        int rem       = totalMins % sessionConfig.getDuration();
        if (rem != 0) {
            dateAndHour = dateAndHour.plusMinutes(sessionConfig.getDuration() - rem);
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
            cursor = cursor.plusMinutes(sessionConfig.getDuration());
        }
        return slots;

    }

}
