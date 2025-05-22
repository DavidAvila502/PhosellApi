package com.dev.phosell.authentication.application.service;

import com.dev.phosell.authentication.infrastructure.security.CustomUserDetails;
import com.dev.phosell.authentication.domain.model.RefreshToken;
import com.dev.phosell.authentication.application.dto.LoginResponseDto;
import com.dev.phosell.authentication.application.dto.LoginUserDto;
import com.dev.phosell.authentication.infrastructure.security.AuthenticationService;
import com.dev.phosell.authentication.infrastructure.security.JwtService;
import com.dev.phosell.authentication.infrastructure.security.RefreshTokenCookieService;
import com.dev.phosell.authentication.infrastructure.service.SaveRefreshTokenInDbService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final SaveRefreshTokenInDbService saveRefreshTokenInDbService;
    private final RefreshTokenCookieService refreshTokenCookieService;

    public LoginService(
            AuthenticationService authenticationService,
            JwtService jwtService,
            SaveRefreshTokenInDbService saveRefreshTokenInDbService,
            RefreshTokenCookieService refreshTokenCookieService
    ){
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.saveRefreshTokenInDbService = saveRefreshTokenInDbService;
        this.refreshTokenCookieService = refreshTokenCookieService;
    }


    public LoginResponseDto login(LoginUserDto loginUser, HttpServletResponse response){

        CustomUserDetails authenticatedUser = authenticationService.authenticate(loginUser);

        String accessToken = jwtService.generateAccessToken(authenticatedUser);

        String refreshTokenString = jwtService.generateRefreshToken(authenticatedUser);

        RefreshToken refreshTokenObject = saveRefreshTokenInDbService.
                SaveRefreshToken(refreshTokenString, authenticatedUser.getUser());

        Cookie refreshTokenCookie = refreshTokenCookieService.
                generateCookie(refreshTokenObject.getToken(),"/api/auth/refresh");

        response.addCookie(refreshTokenCookie);


        return new LoginResponseDto(
                authenticatedUser.getEmail(),accessToken,jwtService.getAccessTokenExpiration());
    }
}
