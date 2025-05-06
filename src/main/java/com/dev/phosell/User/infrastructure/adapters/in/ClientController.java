package com.dev.phosell.User.infrastructure.adapters.in;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
public class ClientController {
    public ClientController(){
    }
    @GetMapping("/")
    public ResponseEntity<String> registerUser(){
        return ResponseEntity.ok("Success!");

    }

}
