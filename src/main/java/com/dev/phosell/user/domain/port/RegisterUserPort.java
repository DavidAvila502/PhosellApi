package com.dev.phosell.user.domain.port;

import com.dev.phosell.user.domain.model.User;

public interface RegisterUserPort {
    User save(User user);
}
