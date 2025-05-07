package com.dev.phosell.authentication.infrastructure.adapter.in;

import com.dev.phosell.authentication.application.service.*;
import com.dev.phosell.authentication.infrastructure.dto.*;
import com.dev.phosell.authentication.infrastructure.persistence.mapper.AuthUserMapper;
import com.dev.phosell.user.domain.model.User;
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
    private final AuthUserMapper authUserMapper;
    private final RegisterClientService registerClientService;
    private  final RefreshAccessTokenService refreshAccessTokenService;
    private final JwtService jwtService;
    private  final LogoutService logoutService;

    public AuthenticationController(
            LoginService loginService,
            AuthUserMapper authUserMapper,
            RegisterClientService registerClientService,
            RefreshAccessTokenService refreshAccessTokenService,
            JwtService jwtService,
            LogoutService logoutService
    ){
    this.loginService = loginService;
    this.authUserMapper = authUserMapper;
    this.registerClientService = registerClientService;
    this.refreshAccessTokenService = refreshAccessTokenService;
    this.jwtService = jwtService;
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
    public ResponseEntity<RegisterClientResponseDto> registerClient(@RequestBody @Valid RegisterClientDto client)
    {
        User user = authUserMapper.toUser(client);

        User registeredUser = registerClientService.RegisterClient(user);

        RegisterClientResponseDto registerClientResponse = authUserMapper.toRegisterClientResponse(registeredUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(registeredUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(registerClientResponse);
    }


    @PostMapping("/refresh")
    public ResponseEntity<RefreshedAccessTokenDto> refreshToken(
            @CookieValue(name = "refreshToken") String refreshToken
    )
    {
        String refreshedAccessToken = refreshAccessTokenService.refreshAccesstoken(refreshToken);

        RefreshedAccessTokenDto refreshedAccessTokenDto = new RefreshedAccessTokenDto();
        refreshedAccessTokenDto.setJwtToken(refreshedAccessToken);
        refreshedAccessTokenDto.setExpiresIn(jwtService.getAccessTokenExpiration());

        return  ResponseEntity.ok(refreshedAccessTokenDto);

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
