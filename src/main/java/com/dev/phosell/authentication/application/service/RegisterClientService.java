package com.dev.phosell.authentication.application.service;

import com.dev.phosell.authentication.application.dto.RegisterClientDto;
import com.dev.phosell.authentication.application.dto.RegisterClientResponseDto;
import com.dev.phosell.authentication.application.mapper.AuthUserDtoMapper;
import com.dev.phosell.user.application.exception.UserExistsException;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.domain.port.UserPersistencePort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class RegisterClientService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthUserDtoMapper authUserDtoMapper;
    private final UserPersistencePort userPersistencePort;

    public RegisterClientService(
            BCryptPasswordEncoder passwordEncoder,
            AuthUserDtoMapper authUserDtoMapper,
            UserPersistencePort userPersistencePort
    ){
        this.passwordEncoder = passwordEncoder;
        this.authUserDtoMapper = authUserDtoMapper;
        this.userPersistencePort = userPersistencePort;
    }

    public RegisterClientResponseDto registerClient(RegisterClientDto clientDto)
    {
        if (userPersistencePort.findByEmail(clientDto.getEmail()).isPresent()) {
            throw new UserExistsException(clientDto.getEmail());
        }

        User newClient = authUserDtoMapper.toUser(clientDto);

        newClient.setRole(Role.CLIENT);

        newClient.validateClient();

        String hashedPassword = passwordEncoder.encode(newClient.getPassword());

        newClient.setPassword(hashedPassword);

        User savedUser = userPersistencePort.save(newClient);

        RegisterClientResponseDto responseDto = authUserDtoMapper.toRegisterClientResponse(savedUser);

        return responseDto;
    }
}
