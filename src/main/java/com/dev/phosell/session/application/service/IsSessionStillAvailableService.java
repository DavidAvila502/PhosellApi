package com.dev.phosell.session.application.service;

import com.dev.phosell.session.domain.exception.InvalidSessionDateException;
import com.dev.phosell.session.domain.exception.InvalidSessionTimeException;
import com.dev.phosell.session.domain.exception.SessionRequestOutOfAllowedHourExcpetion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
public class IsSessionStillAvailableService {
    @Value("${sessions.earliest-booking-hour}")
    private int earliestBookingHour;

    @Value("${sessions.latest-booking-hour}")
    private int latestBookingHour;

    @Value("${sessions.advance-hours}")
    private int advanceHours;

    private final FindSessionsByDateAndPhotographerIdWithStatusesService findSessionsByDateAndPhotographerIdWithStatusesService;

    public IsSessionStillAvailableService(
            FindSessionsByDateAndPhotographerIdWithStatusesService findSessionsByDateAndPhotographerIdWithStatusesService
    ){
        this.findSessionsByDateAndPhotographerIdWithStatusesService = findSessionsByDateAndPhotographerIdWithStatusesService;
    }

    public boolean IsSessionStillAvailable( LocalTime time, LocalDate date)
    {
        // get current date and current time;
        LocalDateTime now   = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDate today   = now.toLocalDate();

        if(date.isBefore(today)){
            throw new InvalidSessionDateException();
        }

        return date.equals(today) ? validateForToday(time,today,now) : validateForTheFuture();
    }

    private Boolean validateForToday(
            LocalTime time,
            LocalDate today,
            LocalDateTime now
            )
    {

        // if the users is trying to request a session in an unallowed hour throw exception
        if(now.isAfter(today.atTime(latestBookingHour,0)) || now.isBefore(today.atTime(earliestBookingHour,0))){
            throw new SessionRequestOutOfAllowedHourExcpetion();
        }

        // get the exact requested session date with time for today
        LocalDateTime requestedSession = today.atTime(time);

        // get next session date with time for today , we give the user 10 tolerance minutes to request
        LocalDateTime nextSession = now.plusHours(advanceHours - 1).plusMinutes(50);

        // if the requested session is before the nextSession means that the user lost his tolerance time
        if(requestedSession.isBefore(nextSession))
        {
            throw  new InvalidSessionTimeException();
        }


        return true;
    }

    private Boolean validateForTheFuture(){
        //TODO: ADD RESTRICTION FOR MIN WORKING HOUR

        return true;
    }
}
