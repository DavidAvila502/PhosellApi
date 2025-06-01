package com.dev.phosell.authentication.domain.port;

import com.dev.phosell.user.domain.model.User;

public interface CurrentUserPort {
    User getAuthenticatedUser();
}
