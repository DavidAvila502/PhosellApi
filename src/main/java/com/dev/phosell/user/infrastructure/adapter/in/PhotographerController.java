package com.dev.phosell.user.infrastructure.adapter.in;

import com.dev.phosell.user.application.dto.PhotographerResponseDto;
import com.dev.phosell.user.application.service.FindAvailablePhotographersAtDateAndTimeService;
import com.dev.phosell.user.application.service.PhotographerMeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/photographers")
public class PhotographerController {

    private final PhotographerMeService photographerMeService;
    private final FindAvailablePhotographersAtDateAndTimeService findAvailablePhotographersAtDateAndTimeService;

    public PhotographerController(
            PhotographerMeService photographerMeService,
            FindAvailablePhotographersAtDateAndTimeService findAvailablePhotographersAtDateAndTimeService
            ){
        this.photographerMeService = photographerMeService;
        this.findAvailablePhotographersAtDateAndTimeService = findAvailablePhotographersAtDateAndTimeService;
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
