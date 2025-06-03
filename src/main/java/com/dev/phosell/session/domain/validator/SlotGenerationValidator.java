package com.dev.phosell.session.domain.validator;

import java.time.Clock;
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
    private final Clock clock;

    public SlotGenerationValidator(
            int earliestBookingHour,
            int latestBookingHour,
            int earliestStartWorkingHour,
            int latestStartWorkingHour,
            int advanceHours,
            int duration,
            Clock clock
    ) {
        this.earliestBookingHour = earliestBookingHour;
        this.latestBookingHour = latestBookingHour;
        this.earliestStartWorkingHour = earliestStartWorkingHour;
        this.latestStartWorkingHour = latestStartWorkingHour;
        this.advanceHours = advanceHours;
        this.duration = duration;
        this.clock = clock;
    }

    public Boolean validateSlotForToday(LocalDateTime slot){
        LocalDateTime now   = LocalDateTime.now(clock).truncatedTo(ChronoUnit.MINUTES);
        LocalDate today   = now.toLocalDate();


        if (slot.isBefore(now)) {
            return false;
        }

        LocalDateTime earliestBooking = today.atTime(earliestBookingHour, 0);
        LocalDateTime latestBooking   = today.atTime(latestBookingHour,   0);
        if (now.isBefore(earliestBooking) || now.isAfter(latestBooking)) {
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
