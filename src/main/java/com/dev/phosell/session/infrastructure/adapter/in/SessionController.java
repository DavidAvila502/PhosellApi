package com.dev.phosell.session.infrastructure.adapter.in;

import com.dev.phosell.session.application.dto.*;
import com.dev.phosell.session.application.service.*;
import com.dev.phosell.session.infrastructure.persistence.mapper.SessionMapper;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
    public final CancelSessionService cancelSessionService;
    public final CompleteSessionService completeSessionService;

    public SessionController(
            FindAllSessionsService findAllSessionsService,
            SessionMapper sessionMapper,
            GetAvailableSessionSlotsService getAvailableSessionSlotsService,
            RegisterSessionService registerSessionService,
            ChangeSessionStatusService changeSessionStatusService,
            CancelSessionService cancelSessionService,
            CompleteSessionService completeSessionService
    )
    {
        this.findAllSessionsService = findAllSessionsService;
        this.sessionMapper = sessionMapper;
        this.getAvailableSessionSlotsService = getAvailableSessionSlotsService;
        this.registerSessionService = registerSessionService;
        this.changeSessionStatusService = changeSessionStatusService;
        this.cancelSessionService = cancelSessionService;
        this.completeSessionService = completeSessionService;
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    {
        return ResponseEntity.ok(getAvailableSessionSlotsService.getAvailableSlots(date));
    }

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

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> changeSessionStatus(
            @PathVariable UUID id,
           @Valid @RequestBody SessionStatusChangeDto statusChange)
    {
        changeSessionStatusService.ChangeStatus(id, statusChange);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelSession(
            @PathVariable UUID id,
            @RequestBody SessionCancelDto sessionCancelDto
    ){
        cancelSessionService.cancel(id,sessionCancelDto);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> uploadPhotosLink(
            @PathVariable UUID id,
            @RequestBody @Valid CompleteSessionDto completeSessionDto
    )
    {
        completeSessionService.complete(id,completeSessionDto);
        return ResponseEntity.ok().build();
    }


    // - Advance endpoints (Admin)

    @GetMapping
    public ResponseEntity<List<SessionResponseDto>> findAll(){
        List<SessionResponseDto> sessions = findAllSessionsService.findAll();
        return  ResponseEntity.ok(sessions);
    }

}
