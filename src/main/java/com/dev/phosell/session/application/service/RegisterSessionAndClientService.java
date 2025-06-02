package com.dev.phosell.session.application.service;

import com.dev.phosell.authentication.application.dto.*;
import com.dev.phosell.authentication.application.service.LoginService;
import com.dev.phosell.authentication.application.service.RegisterClientService;
import com.dev.phosell.session.application.dto.SessionAndClientInsertDto;
import com.dev.phosell.session.application.dto.SessionInsertDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RegisterSessionAndClientService {
    private final RegisterClientService registerClientService;
    private final RegisterSessionService registerSessionService;
    private final LoginService loginService;

    public RegisterSessionAndClientService(
            RegisterClientService registerClientService,
            RegisterSessionService registerSessionService,
            LoginService loginService
    )
    {
        this.registerClientService = registerClientService;
        this.registerSessionService = registerSessionService;
        this.loginService = loginService;
    }

    public LoginTokensGeneratedDto registerSessionAndClient(SessionAndClientInsertDto sessionAndClientInsertDto){

        RegisterClientDto registerClientDto = sessionAndClientInsertDto.getRegisterClientDto();

        SessionInsertDto sessionInsertDto = sessionAndClientInsertDto.getSessionInsertDto();

        RegisterClientResponseDto clientRegistered = registerClientService.registerClient(registerClientDto);

        sessionInsertDto.setClientId(clientRegistered.getId());

        registerSessionService.RegisterSession(sessionInsertDto);

        LoginUserDto loginUserDto = new LoginUserDto(registerClientDto.getEmail(),registerClientDto.getPassword());

        LoginTokensGeneratedDto loginTokens = loginService.login(loginUserDto);

        return loginTokens;
    }
}
