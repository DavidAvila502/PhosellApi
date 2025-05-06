package com.dev.phosell.User.infrastructure.adapters.in;
import com.dev.phosell.User.application.services.ClientServices.ClientMeService;
import com.dev.phosell.User.domain.models.User;
import com.dev.phosell.User.infrastructure.dto.ClientResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
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
