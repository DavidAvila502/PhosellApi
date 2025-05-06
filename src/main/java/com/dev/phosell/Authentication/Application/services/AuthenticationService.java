package com.dev.phosell.Authentication.Application.services;

import com.dev.phosell.Authentication.domain.exception.refreshToken.InvalidUserOrPasswordException;
import com.dev.phosell.Authentication.domain.models.CustomUserDetails;
import com.dev.phosell.Authentication.infrastructure.dto.LoginUserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            CustomUserDetailsService customUserDetailsService,
            AuthenticationManager authenticationManager
    ) {
        this.customUserDetailsService = customUserDetailsService;
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
        return customUserDetailsService.loadUserByUsername(input.getEmail());
    }
}