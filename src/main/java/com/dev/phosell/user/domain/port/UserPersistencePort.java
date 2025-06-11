package com.dev.phosell.user.domain.port;

import com.dev.phosell.user.application.dto.UserFiltersDto;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPersistencePort {
    Optional<User> findById(UUID id);
    User save(User user);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findPhotographersByIsInService(Boolean isInService);
    Page<User> findByFilters(UserFiltersDto filters, Pageable pageable);
}
