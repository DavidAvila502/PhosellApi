package com.dev.phosell.user.infrastructure.adapter.in;

import com.dev.phosell.user.application.dto.PhotographerInsertDto;
import com.dev.phosell.user.application.dto.PhotographerResponseDto;
import com.dev.phosell.user.application.service.FindAvailablePhotographersAtDateAndTimeService;
import com.dev.phosell.user.application.service.PhotographerMeService;
import com.dev.phosell.user.application.service.RegisterPhotographerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/photographers")
public class PhotographerController {

    private final PhotographerMeService photographerMeService;
    private final FindAvailablePhotographersAtDateAndTimeService findAvailablePhotographersAtDateAndTimeService;
    private final RegisterPhotographerService registerPhotographerService;

    public PhotographerController(
            PhotographerMeService photographerMeService,
            FindAvailablePhotographersAtDateAndTimeService findAvailablePhotographersAtDateAndTimeService,
            RegisterPhotographerService registerPhotographerService
            ){
        this.photographerMeService = photographerMeService;
        this.findAvailablePhotographersAtDateAndTimeService = findAvailablePhotographersAtDateAndTimeService;
        this.registerPhotographerService = registerPhotographerService;
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

}
