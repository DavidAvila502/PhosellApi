package com.dev.phosell.user.application.service;

import com.dev.phosell.user.application.dto.PhotographerResponseDto;
import com.dev.phosell.user.application.exception.UserNotFoundException;
import com.dev.phosell.user.application.mapper.UserDtoMapper;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.domain.port.UserPersistencePort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateIsInServiceService {

    private final UserPersistencePort userPersistencePort;
    private final UserDtoMapper userDtoMapper;

    public UpdateIsInServiceService(UserPersistencePort userPersistencePort,UserDtoMapper userDtoMapper){
        this.userPersistencePort = userPersistencePort;
        this.userDtoMapper = userDtoMapper;
    }

    public PhotographerResponseDto update(UUID photographerId, boolean newIsInService)
    {
        User photographer = userPersistencePort.findById(photographerId)
                .orElseThrow(() -> new UserNotFoundException("id",photographerId.toString()));

        photographer.setInService(newIsInService);

        photographer.validatePhotographer();

        userPersistencePort.save(photographer);

        return userDtoMapper.toPhotographerResponse(photographer);
    }
}
