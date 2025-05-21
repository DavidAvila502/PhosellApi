package com.dev.phosell.session.domain.validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SlotGenerationValidator {

    private final int earliestBookingHour;
    private final int latestBookingHour;
    private final int earliestStartWorkingHour;
    private final int latestStartWorkingHour;
    private final int advanceHours;
    private final int duration;;

    public SlotGenerationValidator(int earliestBookingHour, int latestBookingHour, int latestStartWorkingHour, int earliestStartWorkingHour, int advanceHours, int duration) {
        this.earliestBookingHour = earliestBookingHour;
        this.latestBookingHour = latestBookingHour;
        this.latestStartWorkingHour = latestStartWorkingHour;
        this.earliestStartWorkingHour = earliestStartWorkingHour;
        this.advanceHours = advanceHours;
        this.duration = duration;
    }

    public Boolean validateSlotForToday(LocalDateTime slot){
        LocalDateTime now   = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate today   = now.toLocalDate();


        if (slot.isBefore(now)) {
            return false;
        }

        LocalDateTime earliestBooking = today.atTime(earliestBookingHour, 0);
        LocalDateTime latestBooking   = today.atTime(latestBookingHour,   0);
        if (slot.isBefore(earliestBooking) || slot.isAfter(latestBooking)) {
            return  false;
        }

        if (slot.isBefore(now.plusHours(advanceHours))) {
            return false;
        }

        LocalDateTime earliestStart = today.atTime(earliestStartWorkingHour, 0);
        LocalDateTime latestStart   = today.atTime(latestStartWorkingHour,   0);
        if (slot.isBefore(earliestStart) || slot.isAfter(latestStart)) {
            return false;
        }

        return true;


    }
}
