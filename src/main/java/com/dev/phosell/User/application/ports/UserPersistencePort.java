package com.dev.phosell.User.application.ports;

import com.dev.phosell.User.domain.models.Role;
import com.dev.phosell.User.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserPersistencePort {
    User save(User user);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
}
