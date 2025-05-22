package com.dev.phosell.authentication.infrastructure.persistence.mapper;

import com.dev.phosell.authentication.domain.model.RefreshToken;
import com.dev.phosell.authentication.infrastructure.persistence.jpa.entity.RefreshTokenEntity;
import com.dev.phosell.user.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenMapper {

    private final UserMapper userMapper;

    public  RefreshTokenMapper(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public RefreshToken toDomain(RefreshTokenEntity refreshTokenEntity){
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setId(refreshTokenEntity.getId());
        refreshToken.setToken(refreshTokenEntity.getToken());
        refreshToken.setUser(userMapper.toDomain(refreshTokenEntity.getUser()));
        refreshToken.setExpiryDate(refreshTokenEntity.getExpiryDate());

        return  refreshToken;
    }

    public RefreshTokenEntity tokenEntity(RefreshToken refreshToken){
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();

        refreshTokenEntity.setId(refreshToken.getId());
        refreshTokenEntity.setToken(refreshToken.getToken());
        refreshTokenEntity.setUser(userMapper.toEntity(refreshToken.getUser()));
        refreshTokenEntity.setExpiryDate(refreshToken.getExpiryDate());

        return  refreshTokenEntity;
    }
}
