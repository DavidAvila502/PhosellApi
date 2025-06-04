package com.dev.phosell.user.domain.port;

import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPersistencePort {
    Optional<User> findById(UUID id);
    User save(User user);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findPhotographersByIsInService(Boolean isInService);
}
