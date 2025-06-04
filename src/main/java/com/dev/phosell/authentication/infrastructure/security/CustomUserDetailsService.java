package com.dev.phosell.authentication.infrastructure.security;

import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.domain.port.UserPersistencePort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService{

    private final UserPersistencePort userPersistencePort;

    public CustomUserDetailsService(UserPersistencePort userPersistencePort){
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) {

        User user = userPersistencePort.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
}
