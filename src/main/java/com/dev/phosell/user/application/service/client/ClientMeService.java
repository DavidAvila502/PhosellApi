package com.dev.phosell.user.application.service.client;

import com.dev.phosell.authentication.domain.model.CustomUserDetails;
import com.dev.phosell.user.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ClientMeService {

    public ClientMeService(){
    }

    public User me(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

       CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

       User client = userDetails.getUser();

       return  client;

    }


}
