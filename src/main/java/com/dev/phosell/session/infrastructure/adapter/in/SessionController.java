package com.dev.phosell.session.infrastructure.adapter.in;

import com.dev.phosell.session.application.dto.SessionStatusChangeDto;
import com.dev.phosell.session.application.service.ChangeSessionStatusService;
import com.dev.phosell.session.application.service.FindAllSessionsService;
import com.dev.phosell.session.application.service.GetAvailableSessionSlotsService;
import com.dev.phosell.session.application.service.RegisterSessionService;
import com.dev.phosell.session.application.dto.SessionInsertDto;
import com.dev.phosell.session.application.dto.SessionResponseDto;
import com.dev.phosell.session.infrastructure.persistence.mapper.SessionMapper;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {
    public final FindAllSessionsService findAllSessionsService;
    public final SessionMapper sessionMapper;
    public  final GetAvailableSessionSlotsService getAvailableSessionSlotsService;
    public final RegisterSessionService registerSessionService;
    public final ChangeSessionStatusService changeSessionStatusService;

    public SessionController(
            FindAllSessionsService findAllSessionsService,
            SessionMapper sessionMapper,
            GetAvailableSessionSlotsService getAvailableSessionSlotsService,
            RegisterSessionService registerSessionService,
            ChangeSessionStatusService changeSessionStatusService
    )
    {
        this.findAllSessionsService = findAllSessionsService;
        this.sessionMapper = sessionMapper;
        this.getAvailableSessionSlotsService = getAvailableSessionSlotsService;
        this.registerSessionService = registerSessionService;
        this.changeSessionStatusService = changeSessionStatusService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<SessionResponseDto>> findAll(){
        List<SessionResponseDto> sessions = findAllSessionsService.findAll();
        return  ResponseEntity.ok(sessions);
    }


    @PermitAll
    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    {
        return ResponseEntity.ok(getAvailableSessionSlotsService.getAvailableSlots(date));
    }


    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<SessionResponseDto> saveSession(@Valid @RequestBody SessionInsertDto sessionInsert){

        SessionResponseDto savedSession = registerSessionService.RegisterSession(sessionInsert);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedSession.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedSession);
    }

    @PreAuthorize("hasAnyRole('CLIENT','PHOTOGRAPHER','ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeSessionStatus(
            @PathVariable UUID id,
            @RequestBody SessionStatusChangeDto statusChange)
    {
        changeSessionStatusService.ChangeStatus(id, statusChange);

        return ResponseEntity.ok().build();
    }
}
