package com.dev.phosell.user.application.service;

import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.session.domain.service.SessionSlotsAvailabilityCalculator;
import com.dev.phosell.user.application.dto.PhotographerResponseDto;
import com.dev.phosell.user.application.mapper.UserDtoMapper;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.domain.port.UserPersistencePort;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class FindAvailablePhotographersAtDateAndTimeService {
    private final SessionSlotsAvailabilityCalculator sessionSlotsAvailabilityCalculator;
    private final UserPersistencePort userPersistencePort;
    private final SessionPersistencePort sessionPersistencePort;
    private final UserDtoMapper userDtoMapper;

    public FindAvailablePhotographersAtDateAndTimeService(
            SessionSlotsAvailabilityCalculator sessionSlotsAvailabilityCalculator,
            UserPersistencePort userPersistencePort,
            SessionPersistencePort sessionPersistencePort,
            UserDtoMapper userDtoMapper
    )
    {
        this.sessionSlotsAvailabilityCalculator = sessionSlotsAvailabilityCalculator;
        this.userPersistencePort = userPersistencePort;
        this.sessionPersistencePort = sessionPersistencePort;
        this.userDtoMapper = userDtoMapper;
    }

    public List<PhotographerResponseDto> findPhotographers(LocalDate date, LocalTime time)
    {

        List<User> photographersInService = userPersistencePort.findPhotographersByIsInService(true);

        List<String> freeStatuses = List.of(
                SessionStatus.COMPLETED.toString(),
                SessionStatus.CANCELLED_BY_ADMIN.toString(),
                SessionStatus.CANCELLED_BY_CLIENT.toString()
        );

        List<Session> busySessions = sessionPersistencePort.findByDateAndStatusNotIn(date,freeStatuses);

        List<User> freePhotographers =
                sessionSlotsAvailabilityCalculator
                        .CalculateAvailablePhotographersAtTime(busySessions,photographersInService,time);

        return freePhotographers.stream().map( user -> userDtoMapper.toPhotographerResponse(user)).toList();
    }
}
