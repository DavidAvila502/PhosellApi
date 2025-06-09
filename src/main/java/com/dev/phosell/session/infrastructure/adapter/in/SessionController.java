package com.dev.phosell.session.infrastructure.adapter.in;

import com.dev.phosell.authentication.application.dto.LoginResponseDto;
import com.dev.phosell.authentication.application.dto.LoginTokensGeneratedDto;
import com.dev.phosell.authentication.domain.port.CurrentUserPort;
import com.dev.phosell.authentication.infrastructure.security.RefreshTokenCookieService;
import com.dev.phosell.session.application.dto.*;
import com.dev.phosell.session.application.service.*;
import com.dev.phosell.session.infrastructure.persistence.mapper.SessionMapper;
import com.dev.phosell.user.domain.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
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
    private final CurrentUserPort currentUserPort;
    private final RefreshTokenCookieService refreshTokenCookieService;
    private final SwapSessionPhotographerService swapSessionPhotographerService;
    private final ReassignPhotographerService reassignPhotographerService;

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
            RegisterSessionAndClientService registerSessionAndClientService,
            CurrentUserPort currentUserPort,
            RefreshTokenCookieService refreshTokenCookieService,
            SwapSessionPhotographerService swapSessionPhotographerService,
            ReassignPhotographerService reassignPhotographerService
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
        this.currentUserPort = currentUserPort;
        this.refreshTokenCookieService = refreshTokenCookieService;
        this.swapSessionPhotographerService = swapSessionPhotographerService;
        this.reassignPhotographerService = reassignPhotographerService;
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    {
        return ResponseEntity.ok(getAvailableSessionSlotsService.getAvailableSlots(date));
    }

    @PostMapping
    public ResponseEntity<SessionResponseDto> saveSession(
            @Valid @RequestBody SessionInsertDto sessionInsert
    ){

        User authenticatedUser = currentUserPort.getAuthenticatedUser();

        if(!authenticatedUser.getId().equals(sessionInsert.getClientId()))
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
        LoginTokensGeneratedDto loginTokens = registerSessionAndClientService.registerSessionAndClient(dto);

        Cookie refreshTokenCookie = refreshTokenCookieService.
                generateCookie(loginTokens.getRefreshToken().getToken(),"/api/auth/refresh");

        response.addCookie(refreshTokenCookie);

        LoginResponseDto loginResponse = new LoginResponseDto(
                loginTokens.getUserName(),
                loginTokens.getAccessToken(),
                loginTokens.getAccessTokenExpiresIn());

        return ResponseEntity.ok().body(loginResponse);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SessionResponseDto> changeSessionStatus(
            @PathVariable UUID id,
           @Valid @RequestBody SessionStatusChangeDto statusChange)
    {
        User authenticatedUser = currentUserPort.getAuthenticatedUser();

      SessionResponseDto responseDto =  changeSessionStatusService.ChangeStatus(id, statusChange,authenticatedUser);

        return ResponseEntity.ok().body(responseDto);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelSession(
            @PathVariable UUID id,
            @RequestBody SessionCancelDto sessionCancelDto
    ){
        User authenticatedUser = currentUserPort.getAuthenticatedUser();

        cancelSessionService.cancel(id,sessionCancelDto,authenticatedUser);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> uploadPhotosLink(
            @PathVariable UUID id,
            @RequestBody @Valid CompleteSessionDto completeSessionDto
    )
    {
        User authenticatedUser = currentUserPort.getAuthenticatedUser();
        completeSessionService.complete(id,completeSessionDto,authenticatedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/photographer/me")
    public ResponseEntity<Page<SessionResponseDto>> photographerSessionsMe(
            Pageable pageable,
            @RequestParam(required = false) LocalDate date
    )
    {
        User authenticatedUser = currentUserPort.getAuthenticatedUser();
        Page<SessionResponseDto> sessions = getPhotographerMeSessionService.getSessions(date,pageable,authenticatedUser);
        return ResponseEntity.ok().body(sessions);
    }

    @GetMapping("/client/me")
    public ResponseEntity<Page<SessionResponseDto>> clientSessionsMe(
            Pageable pageable,
            @RequestParam(required = false) LocalDate date
    )
    {
        User authenticatedUser = currentUserPort.getAuthenticatedUser();
        Page<SessionResponseDto> sessions = getClientMeSessionService.getSessions(date,pageable,authenticatedUser);

        return  ResponseEntity.ok().body(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionResponseDto> findSessionById(
            @PathVariable UUID id
    ){
        User authenticatedUser = currentUserPort.getAuthenticatedUser();
        SessionResponseDto response = findSessionByIdService.findSession(id,authenticatedUser);

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

    @PostMapping("/swap-photographers")
    public  ResponseEntity<Void> swapPhotographers(
            @RequestBody @Valid SwapSessionPhotographersDto swapDto
    ){
        swapSessionPhotographerService.swapPhotographers(swapDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/reassign-photographer")
    public ResponseEntity<Void> reassignPhotographer(
            @PathVariable UUID id,
            @RequestBody @Valid ReassignPhotographerDto dto
    )
    {
        reassignPhotographerService.reassign(id,dto.getNewPhotographerId());
        return ResponseEntity.ok().build();
    }
}
