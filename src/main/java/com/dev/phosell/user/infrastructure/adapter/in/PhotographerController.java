package com.dev.phosell.user.infrastructure.adapter.in;

import com.dev.phosell.user.application.dto.PhotographerResponseDto;
import com.dev.phosell.user.application.service.PhotographerMeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/photographers")
public class PhotographerController {

    private final PhotographerMeService photographerMeService;

    public PhotographerController(PhotographerMeService photographerMeService){
        this.photographerMeService = photographerMeService;
    }

    @GetMapping("/me")
    public ResponseEntity<PhotographerResponseDto> me(){

        PhotographerResponseDto photographerResponseDto = photographerMeService.me();

        return ResponseEntity.ok(photographerResponseDto);
    }

}
