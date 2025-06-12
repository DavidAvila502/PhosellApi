package com.dev.phosell.user.infrastructure.adapter.in;

import com.dev.phosell.authentication.domain.port.CurrentUserPort;
import com.dev.phosell.user.application.dto.IsInServiceDto;
import com.dev.phosell.user.application.dto.PhotographerInsertDto;
import com.dev.phosell.user.application.dto.PhotographerResponseDto;
import com.dev.phosell.user.application.service.FindAvailablePhotographersAtDateAndTimeService;
import com.dev.phosell.user.application.service.PhotographerMeService;
import com.dev.phosell.user.application.service.RegisterPhotographerService;
import com.dev.phosell.user.application.service.UpdateIsInServiceService;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/photographers")
public class PhotographerController {

    private final PhotographerMeService photographerMeService;
    private final FindAvailablePhotographersAtDateAndTimeService findAvailablePhotographersAtDateAndTimeService;
    private final RegisterPhotographerService registerPhotographerService;
    private final CurrentUserPort currentUserPort;
    private final UpdateIsInServiceService updateIsInServiceService;

    public PhotographerController(
            PhotographerMeService photographerMeService,
            FindAvailablePhotographersAtDateAndTimeService findAvailablePhotographersAtDateAndTimeService,
            RegisterPhotographerService registerPhotographerService,
            CurrentUserPort currentUserPort,
            UpdateIsInServiceService updateIsInServiceService
            ){
        this.photographerMeService = photographerMeService;
        this.findAvailablePhotographersAtDateAndTimeService = findAvailablePhotographersAtDateAndTimeService;
        this.registerPhotographerService = registerPhotographerService;
        this.currentUserPort = currentUserPort;
        this.updateIsInServiceService = updateIsInServiceService;
    }

    @PostMapping
    public ResponseEntity<PhotographerResponseDto> registerPhotographer(
            @RequestBody @Valid PhotographerInsertDto insertDto
            )
    {
        PhotographerResponseDto responseDto =registerPhotographerService.register(insertDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping("/me")
    public ResponseEntity<PhotographerResponseDto> me(){

        PhotographerResponseDto photographerResponseDto = photographerMeService.me();

        return ResponseEntity.ok(photographerResponseDto);
    }

    @GetMapping("/available")
    public ResponseEntity<List<PhotographerResponseDto>> findAvailable(
            @RequestParam LocalDate date,
            @RequestParam LocalTime time
            )
    {
        List<PhotographerResponseDto> photographers =
                findAvailablePhotographersAtDateAndTimeService.findPhotographers(date,time);

        return ResponseEntity.ok().body(photographers);
    }

    @PatchMapping("/{id}/in-service")
    public ResponseEntity<PhotographerResponseDto> toggleInService(
            @PathVariable UUID id,
            @RequestBody @Valid IsInServiceDto dto
            )
    {
        User authenticatedUser = currentUserPort.getAuthenticatedUser();

        if(authenticatedUser.getRole() == Role.PHOTOGRAPHER ){
            if (!authenticatedUser.getId().equals(id)){
                throw new AuthorizationDeniedException("You can't modify others photographers");
            }
        }

        PhotographerResponseDto responseDto = updateIsInServiceService.update(id,dto.isInService);

        return ResponseEntity.ok().body(responseDto);

    }

}
