package com.dev.phosell.session.infrastructure.adapter.in;

import com.dev.phosell.session.application.service.FindAllSessionsService;
import com.dev.phosell.session.application.service.GetAvailableSessionSlotsService;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.infrastructure.dto.SessionResponseDto;
import com.dev.phosell.session.infrastructure.persistence.mapper.SessionMapper;
import jakarta.annotation.security.PermitAll;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {
    public final FindAllSessionsService findAllSessionsService;
    public final SessionMapper sessionMapper;
    public  final GetAvailableSessionSlotsService getAvailableSessionSlotsService;


    public SessionController(
            FindAllSessionsService findAllSessionsService,
            SessionMapper sessionMapper,
            GetAvailableSessionSlotsService getAvailableSessionSlotsService
    )
    {
        this.findAllSessionsService = findAllSessionsService;
        this.sessionMapper = sessionMapper;
        this.getAvailableSessionSlotsService = getAvailableSessionSlotsService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<SessionResponseDto>> findAll(){
        List<Session> sessions = findAllSessionsService.findAll();

        List<SessionResponseDto> response = sessions.stream()
                .map(session -> sessionMapper.toSessionResponseDto(session)).toList();
        return  ResponseEntity.ok(response);
    }


    @PermitAll
    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    {
        return ResponseEntity.ok(getAvailableSessionSlotsService.getAvailableSlots(date));
    }

}
