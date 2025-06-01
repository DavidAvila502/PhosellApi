package com.dev.phosell.session.infrastructure.adapter.in;

import com.dev.phosell.authentication.application.dto.LoginResponseDto;
import com.dev.phosell.authentication.infrastructure.security.CustomUserDetails;
import com.dev.phosell.session.application.dto.*;
import com.dev.phosell.session.application.service.*;
import com.dev.phosell.session.infrastructure.persistence.mapper.SessionMapper;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
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
    public final GetPhotographerMeSessionService getPhotographerMeSessionService;
    public final GetClientMeSessionService getClientMeSessionService;
    private final FindSessionByIdService findSessionByIdService;
    private final RegisterSessionAndClientService registerSessionAndClientService;

    public SessionController(
            FindAllSessionsService findAllSessionsService,
            SessionMapper sessionMapper,
            GetAvailableSessionSlotsService getAvailableSessionSlotsService,
            RegisterSessionService registerSessionService,
            ChangeSessionStatusService changeSessionStatusService,
            CancelSessionService cancelSessionService,
            CompleteSessionService completeSessionService,
            GetPhotographerMeSessionService getPhotographerMeSessionService,
            GetClientMeSessionService getClientMeSessionService,
            FindSessionByIdService findSessionByIdService,
            RegisterSessionAndClientService registerSessionAndClientService
    )
    {
        this.findAllSessionsService = findAllSessionsService;
        this.sessionMapper = sessionMapper;
        this.getAvailableSessionSlotsService = getAvailableSessionSlotsService;
        this.registerSessionService = registerSessionService;
        this.changeSessionStatusService = changeSessionStatusService;
        this.cancelSessionService = cancelSessionService;
        this.completeSessionService = completeSessionService;
        this.getPhotographerMeSessionService = getPhotographerMeSessionService;
        this.getClientMeSessionService = getClientMeSessionService;
        this.findSessionByIdService = findSessionByIdService;
        this.registerSessionAndClientService = registerSessionAndClientService;
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    {
        return ResponseEntity.ok(getAvailableSessionSlotsService.getAvailableSlots(date));
    }

    @PostMapping
    public ResponseEntity<SessionResponseDto> saveSession(
            @Valid @RequestBody SessionInsertDto sessionInsert,
            Authentication authentication
    ){

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User authenticatedUser = customUserDetails.getUser();
        if(!authenticatedUser.getId().equals(sessionInsert.getClientId())
                || authenticatedUser.getRole() != Role.CLIENT)
        {
            throw new AuthorizationDeniedException("Unauthorized action");
        }

        SessionResponseDto savedSession = registerSessionService.RegisterSession(sessionInsert);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedSession.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedSession);
    }

    @PostMapping("/registrations")
    public ResponseEntity<LoginResponseDto> saveUserAndSession(
            @RequestBody @Valid SessionAndClientInsertDto dto,
            HttpServletResponse response
    ){
        LoginResponseDto loginResponse = registerSessionAndClientService.registerSessionAndClient(dto,response);
        return ResponseEntity.ok().body(loginResponse);
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

    @GetMapping("/photographer/me")
    public ResponseEntity<Page<SessionResponseDto>> photographerSessionsMe(
            Pageable pageable,
            @RequestParam(required = false) LocalDate date
    )
    {
        Page<SessionResponseDto> sessions = getPhotographerMeSessionService.getSessions(date,pageable);
        return ResponseEntity.ok().body(sessions);
    }

    @GetMapping("/client/me")
    public ResponseEntity<Page<SessionResponseDto>> clientSessionsMe(
            Pageable pageable,
            @RequestParam(required = false) LocalDate date
    )
    {
        Page<SessionResponseDto> sessions = getClientMeSessionService.getSessions(date,pageable);

        return  ResponseEntity.ok().body(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionResponseDto> findSessionById(
            @PathVariable UUID id
    ){
        SessionResponseDto response = findSessionByIdService.findSession(id);

        return ResponseEntity.ok().body(response);
    };


    // - Advance endpoints (Admin)

    @GetMapping
    public ResponseEntity<Page<SessionResponseDto>> findAll(
        @ModelAttribute SessionFilterDto sessionFilterDto,
        Pageable pageable
    ){
        Page<SessionResponseDto> sessions = findAllSessionsService.findAll(sessionFilterDto,pageable);

        return  ResponseEntity.ok(sessions);
    }
}
