package com.dev.phosell.session.domain.validator;

import com.dev.phosell.session.domain.exception.slot.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class SessionBookingPolicyValidator {

    private final int earliestBookingHour;
    private final int latestBookingHour;
    private final int earliestStartWorkingHour;
    private final int latestStartWorkingHour;
    private final int advanceHours;
    private final int duration;;

    public SessionBookingPolicyValidator(
            int earliestBookingHour,
            int latestBookingHour,
            int earliestStartWorkingHour,
            int latestStartWorkingHour,
            int advanceHours,
            int duration)
    {
        this.earliestBookingHour = earliestBookingHour;
        this.latestBookingHour = latestBookingHour;
        this.earliestStartWorkingHour = earliestStartWorkingHour;
        this.latestStartWorkingHour = latestStartWorkingHour;
        this.advanceHours = advanceHours;
        this.duration = duration;
    }

    public void validate(LocalDate date, LocalTime time){

        LocalDateTime now   = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate today   = now.toLocalDate();
        LocalDateTime slot = date.atTime(time);

        //TODO: Only O'Clock hours are allowed

        if (date.isBefore(today))
        {
            throw new InvalidSessionDateException(date.toString(),"the date is in the past");
        }

        if(date.equals(today))
        {
            validateForToday(date,now,slot);
        }
        else
        {
            validateForTheFuture(date,slot);
        }
    }

    private void validateForToday (LocalDate today,LocalDateTime now,LocalDateTime slot)
    {
        if (slot.isBefore(now)) {
            throw new InvalidSessionSlotException(slot.toString(),"the slot is before now");
        }

        LocalDateTime earliestBooking = today.atTime(earliestBookingHour, 0);
        LocalDateTime latestBooking   = today.atTime(latestBookingHour,   0);
        if (slot.isBefore(earliestBooking) || slot.isAfter(latestBooking)) {
            throw new InvalidBookingHourException();
        }

        // we give 10 tolerance minutes to the user for the next session
        if (slot.isBefore(now.plusHours(advanceHours - 1).plusMinutes(50))) {
            throw new SessionSlotAlreadyExpiredException();
        }

        LocalDateTime earliestStart = today.atTime(earliestStartWorkingHour, 0);
        LocalDateTime latestStart   = today.atTime(latestStartWorkingHour,   0);
        if (slot.isBefore(earliestStart) || slot.isAfter(latestStart)) {
            throw new SessionSlotOutOfWorkingHourException();
        }

    }


    private void validateForTheFuture(LocalDate date,LocalDateTime slot){

        LocalDateTime earliestStart = date.atTime(earliestStartWorkingHour, 0);
        LocalDateTime latestStart   = date.atTime(latestStartWorkingHour,   0);
        if (slot.isBefore(earliestStart) || slot.isAfter(latestStart)) {
            throw new SessionSlotOutOfWorkingHourException();
        }

    }


}
