package com.dev.phosell.user.application.port.out;

import com.dev.phosell.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface FindUserByIdPort {
    Optional<User> findById(UUID id);
}
