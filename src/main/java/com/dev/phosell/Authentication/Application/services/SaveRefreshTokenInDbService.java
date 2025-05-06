package com.dev.phosell.Authentication.Application.services;

import com.dev.phosell.Authentication.domain.models.RefreshToken;
import com.dev.phosell.Authentication.infrastructure.adapters.out.RefreshTokenJpaAdapter;
import com.dev.phosell.User.domain.models.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class SaveRefreshTokenInDbService {

    private final RefreshTokenJpaAdapter refreshTokenJpaAdapter;
    private final  JwtService jwtService;

    public SaveRefreshTokenInDbService(
            RefreshTokenJpaAdapter refreshTokenJpaAdapter,
            JwtService jwtService
    ){
        this.refreshTokenJpaAdapter = refreshTokenJpaAdapter;
        this.jwtService = jwtService;
    }

    public RefreshToken SaveRefreshToken(String token, User user){

        RefreshToken newRefreshToken = new RefreshToken();

        newRefreshToken.setToken(token);
        newRefreshToken.setUser(user);
        newRefreshToken.setExpiryDate(new Timestamp(System.currentTimeMillis() + jwtService.getRefreshTokenExpiration()));

        refreshTokenJpaAdapter.save(newRefreshToken);

        return  newRefreshToken;

    }


}
