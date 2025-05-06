package com.dev.phosell.authentication.infrastructure.persistence.mapper;

import com.dev.phosell.authentication.infrastructure.dto.RegisterClientDto;
import com.dev.phosell.authentication.infrastructure.dto.RegisterClientResponseDto;
import com.dev.phosell.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthUserMapper {

    public User toUser(RegisterClientDto registerClientDto){

        User user = new User();

        user.setFullName(registerClientDto.getFullName());
        user.setEmail(registerClientDto.getEmail());
        user.setPassword(registerClientDto.getPassword());
        user.setPhone(registerClientDto.getPhone());
        user.setCity(registerClientDto.getCity());

        return user;
    }

    public RegisterClientResponseDto toRegisterClientResponse(User user){
        RegisterClientResponseDto registerClientResponse = new RegisterClientResponseDto();

        registerClientResponse.setId(user.getId());
        registerClientResponse.setFullName(user.getFullName());
        registerClientResponse.setEmail(user.getEmail());
        registerClientResponse.setPhone(user.getPhone());
        registerClientResponse.setCity(user.getCity());

        return  registerClientResponse;


    }
}
