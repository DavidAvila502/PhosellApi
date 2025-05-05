package com.dev.phosell.Authentication.Application.ports.out;

import com.dev.phosell.Authentication.domain.models.RefreshToken;

import java.util.Optional;

public interface RefreshTokenPersistencePort {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
}
