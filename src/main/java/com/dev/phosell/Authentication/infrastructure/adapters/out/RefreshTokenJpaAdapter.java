package com.dev.phosell.Authentication.infrastructure.adapters.out;

import com.dev.phosell.Authentication.Application.ports.out.RefreshTokenPersistencePort;
import com.dev.phosell.Authentication.domain.models.RefreshToken;
import com.dev.phosell.Authentication.infrastructure.persistence.jpa.entities.RefreshTokenEntity;
import com.dev.phosell.Authentication.infrastructure.persistence.mappers.RefreshTokenMapper;
import com.dev.phosell.Authentication.infrastructure.persistence.jpa.repositories.RefreshTokenJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RefreshTokenJpaAdapter implements RefreshTokenPersistencePort {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final RefreshTokenMapper refreshTokenMapper;

    public RefreshTokenJpaAdapter(
            RefreshTokenJpaRepository refreshTokenJpaRepository,
            RefreshTokenMapper refreshTokenMapper
    ){
        this.refreshTokenJpaRepository = refreshTokenJpaRepository;
        this.refreshTokenMapper = refreshTokenMapper;

    }
    @Override
    public RefreshToken save(RefreshToken refreshToken) {

        RefreshTokenEntity refreshTokenEntity = refreshTokenMapper.tokenEntity(refreshToken);

        refreshTokenJpaRepository.save(refreshTokenEntity);

        return refreshTokenMapper.toDomain(refreshTokenEntity);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenJpaRepository.findByToken(token);

        if (refreshTokenEntity.isPresent()){
            return Optional.of(refreshTokenMapper.toDomain(refreshTokenEntity.get()));
        }

        return Optional.empty();
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenJpaRepository.deleteByToken(token);
    }
}
