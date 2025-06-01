package com.dev.phosell.session.application.service;

import com.dev.phosell.authentication.infrastructure.security.CustomUserDetails;
import com.dev.phosell.session.application.dto.SessionResponseDto;
import com.dev.phosell.session.application.mapper.SessionDtoMapper;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.user.application.exception.UserNotFoundException;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.domain.port.FindUserByIdPort;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FindSessionsByUserIdService {
    private final SessionPersistencePort sessionPersistencePort;
    private final FindUserByIdPort findUserByIdPort;
    private final SessionDtoMapper sessionDtoMapper;

    public FindSessionsByUserIdService(
            SessionPersistencePort sessionPersistencePort,
            FindUserByIdPort findUserByIdPort,
            SessionDtoMapper sessionDtoMapper
    ){
        this.sessionPersistencePort = sessionPersistencePort;
        this.findUserByIdPort = findUserByIdPort;
        this.sessionDtoMapper = sessionDtoMapper;
    }

    public List<SessionResponseDto> findSessions(UUID userId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User authenticadedUser = customUserDetails.getUser();

        if(authenticadedUser.getRole() != Role.ADMIN){
            throw new AuthorizationDeniedException("Authorization denied");
        }

        User user =  findUserByIdPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("id",userId.toString()));

        List<Session> foundSessions = new ArrayList<>();

        if(user.getRole() == Role.CLIENT)
        {
            foundSessions = sessionPersistencePort.findByClientId(user.getId());
        } else if (user.getRole() == Role.PHOTOGRAPHER)
        {
            foundSessions = sessionPersistencePort.findByPhotographerId(user.getId());
        }

        return foundSessions.stream()
                .map(session -> sessionDtoMapper.toSessionResponseDto(session))
                .toList();
    }
}
