package com.dev.phosell.user.infrastructure.adapter.in;


import com.dev.phosell.user.application.service.UserMeService;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.infrastructure.dto.PhotographerResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/photographers")
public class PhotographerController {

    private final UserMeService userMeService;

    public PhotographerController(UserMeService userMeService){
        this.userMeService = userMeService;
    }

    @GetMapping("/me")
    public ResponseEntity<PhotographerResponseDto> me(){
        User photographer = userMeService.me();

        PhotographerResponseDto photographerResponseDto = new
                PhotographerResponseDto(
                        photographer.getId(),
                        photographer.getFullName(),
                        photographer.getEmail(),
                        photographer.getPhone(),
                        photographer.getAddress(),
                        photographer.getCurp()

        );

        return ResponseEntity.ok(photographerResponseDto);
    }

}
