package com.dev.phosell.user.domain.port;

import com.dev.phosell.user.domain.model.User;
import java.util.List;

public interface FindPhotographersByIsInServicePort {
    List<User> findPhotographersByIsInService(Boolean isInService);
}
