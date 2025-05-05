package com.dev.phosell.Authentication.Application.ports.out;

import com.dev.phosell.users.domain.models.User;

public interface RegisterUserPort {
    User save(User user);
}
