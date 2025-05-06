package com.dev.phosell.User.application.services.ClientServices;

import com.dev.phosell.Authentication.domain.models.CustomUserDetails;
import com.dev.phosell.User.domain.models.User;
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
