package com.dev.phosell.Authentication.Application.services;

import com.dev.phosell.Authentication.domain.exception.refreshToken.InvalidUserOrPasswordException;
import com.dev.phosell.Authentication.domain.models.CustomUserDetails;
import com.dev.phosell.Authentication.infrastructure.dto.LoginUserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserDetailsService userDetailsService,
            AuthenticationManager authenticationManager
    ) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    public CustomUserDetails authenticate(LoginUserDto input) {
        // try to authenticate the user

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword())
            );
        }catch (BadCredentialsException ex){
            throw new InvalidUserOrPasswordException();
        }

        // return the user
        return (CustomUserDetails) userDetailsService.loadUserByUsername(input.getEmail());
    }
}