package com.dev.phosell.Authentication.Application.services;

import com.dev.phosell.Authentication.Application.ports.out.LoadUserPort;
import com.dev.phosell.Authentication.domain.models.CustomUserDetails;
import com.dev.phosell.User.domain.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final LoadUserPort loadUserPort;

    public CustomUserDetailsService(LoadUserPort loadUserPort){
        this.loadUserPort = loadUserPort;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) {

        User user = loadUserPort.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
}
