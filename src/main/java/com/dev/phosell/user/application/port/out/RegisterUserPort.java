package com.dev.phosell.user.application.port.out;

import com.dev.phosell.user.domain.model.User;

public interface RegisterUserPort {
    User save(User user);
}
