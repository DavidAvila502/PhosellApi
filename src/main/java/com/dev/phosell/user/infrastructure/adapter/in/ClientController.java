package com.dev.phosell.user.infrastructure.adapter.in;

import com.dev.phosell.user.application.service.ClientMeService;
import com.dev.phosell.user.application.dto.ClientResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientMeService clientMeService;

    public ClientController(ClientMeService clientMeService){
            this.clientMeService = clientMeService;
    }

    @GetMapping("/me")
    public ResponseEntity<ClientResponseDto> me(){

        ClientResponseDto clientResponseDto = clientMeService.me();

        return ResponseEntity.ok(clientResponseDto);
    }

}
