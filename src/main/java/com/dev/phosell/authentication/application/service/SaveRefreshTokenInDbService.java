package com.dev.phosell.authentication.application.service;

import com.dev.phosell.authentication.domain.model.RefreshToken;
import com.dev.phosell.authentication.infrastructure.adapter.out.RefreshTokenJpaAdapter;
import com.dev.phosell.user.domain.model.User;
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
