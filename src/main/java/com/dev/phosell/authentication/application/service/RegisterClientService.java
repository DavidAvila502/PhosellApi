package com.dev.phosell.authentication.application.service;

import com.dev.phosell.authentication.application.dto.RegisterClientDto;
import com.dev.phosell.authentication.application.dto.RegisterClientResponseDto;
import com.dev.phosell.authentication.application.mapper.AuthUserDtoMapper;
import com.dev.phosell.user.domain.port.FindUserByEmailPort;
import com.dev.phosell.user.domain.port.RegisterUserPort;
import com.dev.phosell.user.application.exception.UserExistsException;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class RegisterClientService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final FindUserByEmailPort findUserByEmailPort;
    private  final RegisterUserPort registerUserPort;
    private final AuthUserDtoMapper authUserDtoMapper;

    public RegisterClientService(
            BCryptPasswordEncoder passwordEncoder,
            FindUserByEmailPort findUserByEmailPort,
            RegisterUserPort registerUserPort,
            AuthUserDtoMapper authUserDtoMapper
    ){
        this.passwordEncoder = passwordEncoder;
        this.findUserByEmailPort = findUserByEmailPort;
        this.registerUserPort = registerUserPort;
        this.authUserDtoMapper = authUserDtoMapper;
    }

    public RegisterClientResponseDto registerClient(RegisterClientDto clientDto)
    {
        if (findUserByEmailPort.findByEmail(clientDto.getEmail()).isPresent()) {
            throw new UserExistsException(clientDto.getEmail());
        }

        User newClient = authUserDtoMapper.toUser(clientDto);

        newClient.setRole(Role.CLIENT);

        newClient.validateClient();

        String hashedPassword = passwordEncoder.encode(newClient.getPassword());

        newClient.setPassword(hashedPassword);

        User savedUser = registerUserPort.save(newClient);

        RegisterClientResponseDto responseDto = authUserDtoMapper.toRegisterClientResponse(savedUser);

        return responseDto;
    }
}
