package com.dev.phosell.Authentication.Application.services;

import com.dev.phosell.Authentication.domain.models.CustomUserDetails;
import com.dev.phosell.Authentication.domain.models.RefreshToken;
import com.dev.phosell.Authentication.infrastructure.adapters.out.RefreshTokenJpaAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RefreshAccessTokenService {
    private final RefreshTokenJpaAdapter refreshTokenJpaAdapter;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public RefreshAccessTokenService(
            RefreshTokenJpaAdapter refreshTokenJpaAdapter,
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService
    ){
        this.refreshTokenJpaAdapter = refreshTokenJpaAdapter;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    public String refreshAccesstoken(String refreshToken){

        String userEmail = jwtService.extractUsername(refreshToken);

        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

        // Validate the token
        if(!jwtService.isTokenValid(refreshToken,userDetails)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        };

        // Find the token in database
        RefreshToken refreshTokenFound = refreshTokenJpaAdapter.findByToken(refreshToken)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Generate new access token
        String newAccessToken = jwtService.generateAccessToken(userDetails);

        return  newAccessToken;
    }

}
