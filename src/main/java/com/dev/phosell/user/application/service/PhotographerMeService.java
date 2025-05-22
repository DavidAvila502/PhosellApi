package com.dev.phosell.user.application.service;

import com.dev.phosell.user.application.dto.PhotographerResponseDto;
import com.dev.phosell.user.application.mapper.UserDtoMapper;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.infrastructure.service.UserMeService;
import org.springframework.stereotype.Service;

@Service
public class PhotographerMeService {

    private final UserMeService userMeService;
    private final UserDtoMapper userDtoMapper;

    public PhotographerMeService(UserMeService userMeService, UserDtoMapper userDtoMapper){
        this.userMeService = userMeService;
        this.userDtoMapper = userDtoMapper;
    }

    public PhotographerResponseDto me(){

        User user = userMeService.me();

        return userDtoMapper.toPhotographerResponse(user);
    }
}
