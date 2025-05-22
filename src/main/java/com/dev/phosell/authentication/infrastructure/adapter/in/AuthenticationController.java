package com.dev.phosell.authentication.infrastructure.adapter.in;

import com.dev.phosell.authentication.application.dto.*;
import com.dev.phosell.authentication.application.service.*;
import com.dev.phosell.authentication.application.mapper.AuthUserDtoMapper;
import com.dev.phosell.authentication.infrastructure.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final LoginService loginService;
    private final RegisterClientService registerClientService;
    private  final GetRefreshAccessTokenService getRefreshAccessTokenService;
    private  final LogoutService logoutService;

    public AuthenticationController(
            LoginService loginService,
            AuthUserDtoMapper authUserDtoMapper,
            RegisterClientService registerClientService,
            GetRefreshAccessTokenService getRefreshAccessTokenService,
            JwtService jwtService,
            LogoutService logoutService
    ){
    this.loginService = loginService;
    this.registerClientService = registerClientService;
    this.getRefreshAccessTokenService = getRefreshAccessTokenService;
    this.logoutService = logoutService;
    }

    @PostMapping("/login")
    public  ResponseEntity<LoginResponseDto> authenticate(
            @RequestBody @Valid LoginUserDto loginUser,
            HttpServletResponse response)
    {
        LoginResponseDto loginResponse = loginService.login(loginUser,response);
        return  ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterClientResponseDto> registerClient(@RequestBody @Valid RegisterClientDto registerClientDto)
    {
        RegisterClientResponseDto registeredClient = registerClientService.registerClient(registerClientDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(registeredClient.getId())
                .toUri();
        return ResponseEntity.created(location).body(registeredClient);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshedAccessTokenDto> refreshToken(
            @CookieValue(name = "refreshToken") String refreshToken
    )
    {
        RefreshedAccessTokenDto refreshedAccessToken = getRefreshAccessTokenService.getRefreshAccessToken(refreshToken);

        return  ResponseEntity.ok(refreshedAccessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response)
    {
        logoutService.logout(refreshToken,response);
        return ResponseEntity.ok().build();
    }

}
