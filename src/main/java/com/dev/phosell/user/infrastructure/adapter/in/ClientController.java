package com.dev.phosell.user.infrastructure.adapter.in;
import com.dev.phosell.user.application.service.client.ClientMeService;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.infrastructure.dto.ClientResponseDto;
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
    public ResponseEntity<ClientResponseDto> registerUser(){

        User client = clientMeService.me();

        ClientResponseDto clientResponseDto = new ClientResponseDto(
                client.getId(),
                client.getFullName(),
                client.getEmail(),
                client.getPhone(),
                client.getCity()
                );

        return ResponseEntity.ok(clientResponseDto);

    }

}
