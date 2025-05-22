package com.dev.phosell.user.application.mapper;

import com.dev.phosell.user.application.dto.ClientResponseDto;
import com.dev.phosell.user.application.dto.PhotographerResponseDto;
import com.dev.phosell.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {

    public ClientResponseDto toClientResponse(User user){
        return new  ClientResponseDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getCity()
        );
    }

    public PhotographerResponseDto toPhotographerResponse(User user){
        return new PhotographerResponseDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getCurp()
        );
    }
}
