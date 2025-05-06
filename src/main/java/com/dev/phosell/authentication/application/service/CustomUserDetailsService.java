package com.dev.phosell.authentication.application.service;

import com.dev.phosell.authentication.application.port.out.LoadUserPort;
import com.dev.phosell.authentication.domain.model.CustomUserDetails;
import com.dev.phosell.user.domain.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{

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
