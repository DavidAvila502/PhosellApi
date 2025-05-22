package com.dev.phosell.authentication.application.service;

import com.dev.phosell.authentication.application.dto.RefreshedAccessTokenDto;
import com.dev.phosell.authentication.domain.port.RefreshTokenPersistencePort;
import com.dev.phosell.authentication.infrastructure.security.CustomUserDetails;
import com.dev.phosell.authentication.domain.model.RefreshToken;
import com.dev.phosell.authentication.infrastructure.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GetRefreshAccessTokenService {
    private final RefreshTokenPersistencePort refreshTokenPersistencePort;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public GetRefreshAccessTokenService(
            RefreshTokenPersistencePort refreshTokenPersistencePort,
            JwtService jwtService,
            UserDetailsService customUserDetailsService
    ){
        this.refreshTokenPersistencePort = refreshTokenPersistencePort;
        this.jwtService = jwtService;
        this.userDetailsService = customUserDetailsService;
    }

    public RefreshedAccessTokenDto getRefreshAccessToken(String refreshToken){

        String userEmail = jwtService.extractUsername(refreshToken);

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userEmail);

        if(!jwtService.isTokenValid(refreshToken,userDetails)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        };

        RefreshToken refreshTokenFound = refreshTokenPersistencePort.findByToken(refreshToken)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        String newAccessToken = jwtService.generateAccessToken(userDetails);

        RefreshedAccessTokenDto refreshedAccessTokenDto = new RefreshedAccessTokenDto();
        refreshedAccessTokenDto.setJwtToken(newAccessToken);
        refreshedAccessTokenDto.setExpiresIn(jwtService.getAccessTokenExpiration());

        return  refreshedAccessTokenDto;
    }

}
