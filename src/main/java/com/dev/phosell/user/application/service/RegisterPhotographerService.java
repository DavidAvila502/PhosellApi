package com.dev.phosell.user.application.service;

import com.dev.phosell.user.application.dto.PhotographerInsertDto;
import com.dev.phosell.user.application.dto.PhotographerResponseDto;
import com.dev.phosell.user.application.exception.UserExistsException;
import com.dev.phosell.user.application.mapper.UserDtoMapper;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.domain.port.UserPersistencePort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterPhotographerService {

    private final UserPersistencePort userPersistencePort;
    private final UserDtoMapper userDtoMapper;
    private final BCryptPasswordEncoder passwordEncoder;


    public RegisterPhotographerService(
            UserPersistencePort userPersistencePort,
            UserDtoMapper userDtoMapper,
            BCryptPasswordEncoder passwordEncoder
    ){
        this.userPersistencePort = userPersistencePort;
        this.userDtoMapper = userDtoMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public PhotographerResponseDto register(PhotographerInsertDto insertDto){

       Optional<User> photographerFound =   userPersistencePort.findByEmail(insertDto.getEmail());

       if(photographerFound.isPresent()){
           throw new UserExistsException(insertDto.getEmail());
       }

       User newPhotographer = userDtoMapper.toUser(insertDto);

        newPhotographer.validatePhotographer();

        String hashedPassword = passwordEncoder.encode(newPhotographer.getPassword());

        newPhotographer.setPassword(hashedPassword);

       User photographerSaved = userPersistencePort.save(newPhotographer);

       return userDtoMapper.toPhotographerResponse(photographerSaved);
    }

}

