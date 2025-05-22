package com.dev.phosell.user.domain.port;

import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserPersistencePort {
    User save(User user);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
}
