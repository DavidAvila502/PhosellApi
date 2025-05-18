package com.dev.phosell.authentication.application.service;

import com.dev.phosell.user.application.port.out.FindUserByEmailPort;
import com.dev.phosell.authentication.domain.model.CustomUserDetails;
import com.dev.phosell.user.domain.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
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
