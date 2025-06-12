package com.dev.phosell.authentication.application.service;

import com.dev.phosell.authentication.application.dto.LoginTokensGeneratedDto;
import com.dev.phosell.authentication.infrastructure.security.CustomUserDetails;
import com.dev.phosell.authentication.domain.model.RefreshToken;
import com.dev.phosell.authentication.application.dto.LoginUserDto;
import com.dev.phosell.authentication.infrastructure.security.AuthenticationService;
import com.dev.phosell.authentication.infrastructure.security.JwtService;
import com.dev.phosell.authentication.infrastructure.service.SaveRefreshTokenInDbService;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final SaveRefreshTokenInDbService saveRefreshTokenInDbService;

    public LoginService(
            AuthenticationService authenticationService,
            JwtService jwtService,
            SaveRefreshTokenInDbService saveRefreshTokenInDbService
    ){
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.saveRefreshTokenInDbService = saveRefreshTokenInDbService;
    }


    public LoginTokensGeneratedDto login(LoginUserDto loginUser){

        CustomUserDetails authenticatedUser = authenticationService.authenticate(loginUser);

        String accessToken = jwtService.generateAccessToken(authenticatedUser);

        String refreshTokenString = jwtService.generateRefreshToken(authenticatedUser);

        RefreshToken refreshTokenObject = saveRefreshTokenInDbService.
                SaveRefreshToken(refreshTokenString, authenticatedUser.getUser());

        return new LoginTokensGeneratedDto(
                authenticatedUser.getUser().getId(),
                authenticatedUser.getUser().getFullName(),
                authenticatedUser.getEmail(),
                authenticatedUser.getRole(),
                accessToken,
                jwtService.getAccessTokenExpiration(),
                refreshTokenObject);
    }
}
