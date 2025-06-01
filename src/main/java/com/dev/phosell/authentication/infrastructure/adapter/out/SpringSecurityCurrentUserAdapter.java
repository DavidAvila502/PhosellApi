package com.dev.phosell.authentication.infrastructure.adapter.out;

import com.dev.phosell.authentication.domain.port.CurrentUserPort;
import com.dev.phosell.authentication.infrastructure.security.CustomUserDetails;
import com.dev.phosell.user.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityCurrentUserAdapter implements CurrentUserPort {
    @Override
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AuthenticationException("User is not authenticated") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            };
        }
        CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
        return details.getUser();
    }
}