package com.dev.phosell.authentication.domain.port;

import com.dev.phosell.authentication.domain.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenPersistencePort {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
}
