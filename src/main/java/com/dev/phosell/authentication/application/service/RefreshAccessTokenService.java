package com.dev.phosell.authentication.application.service;

import com.dev.phosell.authentication.domain.model.CustomUserDetails;
import com.dev.phosell.authentication.domain.model.RefreshToken;
import com.dev.phosell.authentication.infrastructure.adapter.out.RefreshTokenJpaAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RefreshAccessTokenService {
    private final RefreshTokenJpaAdapter refreshTokenJpaAdapter;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public RefreshAccessTokenService(
            RefreshTokenJpaAdapter refreshTokenJpaAdapter,
            JwtService jwtService,
            UserDetailsService customUserDetailsService
    ){
        this.refreshTokenJpaAdapter = refreshTokenJpaAdapter;
        this.jwtService = jwtService;
        this.userDetailsService = customUserDetailsService;
    }

    public String refreshAccesstoken(String refreshToken){

        String userEmail = jwtService.extractUsername(refreshToken);

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userEmail);

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
