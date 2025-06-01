package com.dev.phosell.session.application.service;

import com.dev.phosell.authentication.application.dto.RegisterClientDto;
import com.dev.phosell.authentication.application.dto.RegisterClientResponseDto;
import com.dev.phosell.authentication.application.service.RegisterClientService;
import com.dev.phosell.session.application.dto.SessionAndClientInsertDto;
import com.dev.phosell.session.application.dto.SessionInsertDto;
import com.dev.phosell.session.application.dto.SessionResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RegisterSessionAndClientService {
    private final RegisterClientService registerClientService;
    private final RegisterSessionService registerSessionService;

    public RegisterSessionAndClientService(
            RegisterClientService registerClientService,
            RegisterSessionService registerSessionService
    )
    {
        this.registerClientService = registerClientService;
        this.registerSessionService = registerSessionService;
    }

    public SessionResponseDto registerSessionAndClient(SessionAndClientInsertDto sessionAndClientInsertDto){

        RegisterClientDto registerClientDto = sessionAndClientInsertDto.getRegisterClientDto();

        SessionInsertDto sessionInsertDto = sessionAndClientInsertDto.getSessionInsertDto();

        RegisterClientResponseDto clientRegistered = registerClientService.registerClient(registerClientDto);

        sessionInsertDto.setClientId(clientRegistered.getId());

        SessionResponseDto sessionResponseDto = registerSessionService.RegisterSession(sessionInsertDto);

        return sessionResponseDto;
    }
}
