package com.dev.phosell.user.infrastructure.service;

import com.dev.phosell.authentication.infrastructure.security.CustomUserDetails;
import com.dev.phosell.user.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UserMeService {

    public UserMeService(){
    }

    public User me(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User client = userDetails.getUser();

        return  client;

    }


}
