package com.dev.phosell.authentication.infrastructure.adapter.out;

import com.dev.phosell.authentication.application.port.out.RefreshTokenPersistencePort;
import com.dev.phosell.authentication.domain.model.RefreshToken;
import com.dev.phosell.authentication.infrastructure.persistence.jpa.entity.RefreshTokenEntity;
import com.dev.phosell.authentication.infrastructure.persistence.mapper.RefreshTokenMapper;
import com.dev.phosell.authentication.infrastructure.persistence.jpa.repository.RefreshTokenJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
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
