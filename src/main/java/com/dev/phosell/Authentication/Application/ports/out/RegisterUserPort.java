package com.dev.phosell.Authentication.Application.ports.out;

import com.dev.phosell.User.domain.models.User;

public interface RegisterUserPort {
    User save(User user);
}
