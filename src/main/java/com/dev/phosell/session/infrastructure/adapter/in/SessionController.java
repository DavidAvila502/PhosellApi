package com.dev.phosell.session.infrastructure.adapter.in;

import com.dev.phosell.session.application.service.FindAllSessionsService;
import com.dev.phosell.session.domain.model.Session;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionController {

    public final FindAllSessionsService findAllSessionsService;

    public SessionController(FindAllSessionsService findAllSessionsService){
        this.findAllSessionsService = findAllSessionsService;
    }

    @GetMapping
    public ResponseEntity<List<Session>> findAll(){
        List<Session> sessions = findAllSessionsService.findAll();
        return  ResponseEntity.ok(sessions);

    }

}
