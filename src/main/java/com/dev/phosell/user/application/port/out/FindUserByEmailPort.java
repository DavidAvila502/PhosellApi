package com.dev.phosell.user.application.port.out;

import com.dev.phosell.user.domain.model.User;

import java.util.Optional;

public interface FindUserByEmailPort {
    Optional<User> findByEmail(String email);
}
