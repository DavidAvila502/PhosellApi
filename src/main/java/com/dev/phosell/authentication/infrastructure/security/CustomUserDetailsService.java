package com.dev.phosell.authentication.infrastructure.security;

import com.dev.phosell.user.domain.port.FindUserByEmailPort;
import com.dev.phosell.user.domain.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService{

    private final FindUserByEmailPort findUserByEmailPort;

    public CustomUserDetailsService(FindUserByEmailPort findUserByEmailPort){
        this.findUserByEmailPort = findUserByEmailPort;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) {

        User user = findUserByEmailPort.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
}
