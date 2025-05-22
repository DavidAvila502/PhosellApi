package com.dev.phosell.user.application.service;

import com.dev.phosell.user.application.dto.ClientResponseDto;
import com.dev.phosell.user.application.mapper.UserDtoMapper;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.infrastructure.service.UserMeService;
import org.springframework.stereotype.Service;

@Service
public class ClientMeService {

    private final UserMeService userMeService;
    private final UserDtoMapper userDtoMapper;

    public ClientMeService(UserMeService userMeService, UserDtoMapper userDtoMapper){
        this.userMeService = userMeService;
        this.userDtoMapper = userDtoMapper;
    }

    public ClientResponseDto me(){

        User user = userMeService.me();

        return userDtoMapper.toClientResponse(user);
    }

}
