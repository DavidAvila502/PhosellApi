package com.dev.phosell.session.application.service;

import com.dev.phosell.session.application.dto.SessionFilterDto;
import com.dev.phosell.session.application.dto.SessionResponseDto;
import com.dev.phosell.session.application.mapper.SessionDtoMapper;
import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;


@Service
public class FindAllSessionsService {
    private  final SessionPersistencePort sessionPersistencePort;
    private final SessionDtoMapper sessionDtoMapper;

    public FindAllSessionsService(SessionPersistencePort sessionPersistencePort, SessionDtoMapper sessionDtoMapper){
        this.sessionPersistencePort = sessionPersistencePort;
        this.sessionDtoMapper = sessionDtoMapper;
    }
    public Page<SessionResponseDto> findAll(SessionFilterDto sessionFilterDto, Pageable pageable){

        LocalDate date = sessionFilterDto.getDate();
        LocalTime time = sessionFilterDto.getTime();
        UUID clientId = sessionFilterDto.getClientId();
        UUID photographerId = sessionFilterDto.getPhotographerId();
        List<SessionStatus> statuses = SessionStatus.fromListString(sessionFilterDto.getStatuses());

        return sessionPersistencePort
                .findByFilters(date,time,photographerId,clientId,statuses,pageable)
                .map(session -> sessionDtoMapper.toSessionResponseDto(session));
    }
}
