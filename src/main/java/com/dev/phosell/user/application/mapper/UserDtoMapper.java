package com.dev.phosell.user.application.mapper;

import com.dev.phosell.user.application.dto.ClientResponseDto;
import com.dev.phosell.user.application.dto.PhotographerInsertDto;
import com.dev.phosell.user.application.dto.PhotographerResponseDto;
import com.dev.phosell.user.domain.model.Role;
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

    public User toUser(PhotographerInsertDto photographerInsertDto){

        User user = new User();
        user.setFullName(photographerInsertDto.getFullName());
        user.setEmail(photographerInsertDto.getEmail());
        user.setPassword(photographerInsertDto.getPassword());
        user.setPhone(photographerInsertDto.getPhone());
        user.setAddress(photographerInsertDto.getFullAddress());
        user.setCurp(photographerInsertDto.getCurp());
        user.setCity(photographerInsertDto.getCity());
        user.setInService(false);
        user.setRole(Role.PHOTOGRAPHER);

        return user;
    }
}
