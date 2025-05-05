package com.dev.phosell.Authentication.Application.services;

import com.dev.phosell.Authentication.domain.models.CustomUserDetails;
import com.dev.phosell.Authentication.domain.models.RefreshToken;
import com.dev.phosell.Authentication.infrastructure.adapters.out.RefreshTokenJpaAdapter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LogoutService {
    private final RefreshTokenJpaAdapter refreshTokenJpaAdapter;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private  final RefreshTokenCookieService refreshTokenCookieService;

    public  LogoutService(
            RefreshTokenJpaAdapter refreshTokenJpaAdapter,
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService,
            RefreshTokenCookieService refreshTokenCookieService
    ){
        this.refreshTokenJpaAdapter = refreshTokenJpaAdapter;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.refreshTokenCookieService = refreshTokenCookieService;
    }

    @Transactional
    public void logout(String refreshToken, HttpServletResponse response){

        String userEmail = jwtService.extractUsername(refreshToken);

        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

        // Validate the token
        if(!jwtService.isTokenValid(refreshToken,userDetails)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        };

        // Find the token in database
        RefreshToken foundRefreshToken = refreshTokenJpaAdapter.findByToken(refreshToken)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Delete the token from database
        refreshTokenJpaAdapter.deleteByToken(foundRefreshToken.getToken());

        // Clean refreshToken from Cookies
        Cookie refreshTokenCookie = refreshTokenCookieService.cleanCookie("/api/auth/refresh");
        response.addCookie(refreshTokenCookie);

    }

}
