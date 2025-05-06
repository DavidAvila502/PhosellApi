package com.dev.phosell.authentication.application.service;

import com.dev.phosell.authentication.domain.model.CustomUserDetails;
import com.dev.phosell.authentication.domain.model.RefreshToken;
import com.dev.phosell.authentication.infrastructure.dto.LoginResponseDto;
import com.dev.phosell.authentication.infrastructure.dto.LoginUserDto;
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

        // Authenticate the user
        CustomUserDetails authenticatedUser = authenticationService.authenticate(loginUser);

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(authenticatedUser);

        String refreshToken = jwtService.generateRefreshToken(authenticatedUser);

        // Store the refresh token in database
        RefreshToken refreshTokenObject = saveRefreshTokenInDbService.
                SaveRefreshToken(refreshToken, authenticatedUser.getUser());

        // Generate refreshToken Cookie
        Cookie refreshTokenCookie = refreshTokenCookieService.
                generateCookie(refreshTokenObject.getToken(),"/api/auth/refresh");

        //Add refreshTokenCookie to response
        response.addCookie(refreshTokenCookie);


        return new LoginResponseDto(
                authenticatedUser.getEmail(),accessToken,jwtService.getAccessTokenExpiration());

    }


}
