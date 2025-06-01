package com.dev.phosell.session.application.service;

import com.dev.phosell.authentication.application.dto.LoginResponseDto;
import com.dev.phosell.authentication.application.dto.LoginUserDto;
import com.dev.phosell.authentication.application.dto.RegisterClientDto;
import com.dev.phosell.authentication.application.dto.RegisterClientResponseDto;
import com.dev.phosell.authentication.application.service.LoginService;
import com.dev.phosell.authentication.application.service.RegisterClientService;
import com.dev.phosell.session.application.dto.SessionAndClientInsertDto;
import com.dev.phosell.session.application.dto.SessionInsertDto;
import jakarta.servlet.http.HttpServletResponse;
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

    public LoginResponseDto registerSessionAndClient(SessionAndClientInsertDto sessionAndClientInsertDto, HttpServletResponse response){

        RegisterClientDto registerClientDto = sessionAndClientInsertDto.getRegisterClientDto();

        SessionInsertDto sessionInsertDto = sessionAndClientInsertDto.getSessionInsertDto();

        RegisterClientResponseDto clientRegistered = registerClientService.registerClient(registerClientDto);

        sessionInsertDto.setClientId(clientRegistered.getId());

        registerSessionService.RegisterSession(sessionInsertDto);

        LoginUserDto loginUserDto = new LoginUserDto(registerClientDto.getEmail(),registerClientDto.getPassword());

        LoginResponseDto loginResponse = loginService.login(loginUserDto,response);

        return loginResponse;
    }
}
