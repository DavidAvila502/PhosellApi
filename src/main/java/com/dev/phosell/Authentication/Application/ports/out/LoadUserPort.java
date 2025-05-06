package com.dev.phosell.Authentication.Application.ports.out;

import com.dev.phosell.User.domain.models.User;

import java.util.Optional;

public interface LoadUserPort {
    Optional<User> findByEmail(String email);
}
