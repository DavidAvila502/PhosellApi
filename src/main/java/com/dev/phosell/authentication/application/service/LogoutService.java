package com.dev.phosell.authentication.application.service;

import com.dev.phosell.authentication.application.port.out.RefreshTokenPersistencePort;
import com.dev.phosell.authentication.domain.exception.token.InvalidRefreshTokenException;
import com.dev.phosell.authentication.domain.model.CustomUserDetails;
import com.dev.phosell.authentication.domain.model.RefreshToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LogoutService {
    private final RefreshTokenPersistencePort refreshTokenPersistencePort;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private  final RefreshTokenCookieService refreshTokenCookieService;

    public  LogoutService(
            RefreshTokenPersistencePort refreshTokenPersistencePort,
            JwtService jwtService,
            UserDetailsService customUserDetailsService,
            RefreshTokenCookieService refreshTokenCookieService
    ){
        this.refreshTokenPersistencePort = refreshTokenPersistencePort;
        this.jwtService = jwtService;
        this.userDetailsService = customUserDetailsService;
        this.refreshTokenCookieService = refreshTokenCookieService;
    }

    @Transactional
    public void logout(String refreshToken, HttpServletResponse response){

        String userEmail = jwtService.extractUsername(refreshToken);

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userEmail);

        // Validate the token
        if(!jwtService.isTokenValid(refreshToken,userDetails)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        };

        // Find the token in database
        RefreshToken foundRefreshToken = refreshTokenPersistencePort.findByToken(refreshToken)
                .orElseThrow(InvalidRefreshTokenException::new);

        // Delete the token from database
        refreshTokenPersistencePort.deleteByToken(foundRefreshToken.getToken());

        // Clean refreshToken from Cookies
        Cookie refreshTokenCookie = refreshTokenCookieService.cleanCookie("/api/auth/refresh");
        response.addCookie(refreshTokenCookie);

    }

}
