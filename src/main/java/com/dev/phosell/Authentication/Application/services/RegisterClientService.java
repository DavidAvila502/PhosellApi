package com.dev.phosell.Authentication.Application.services;

import com.dev.phosell.Authentication.Application.ports.out.LoadUserPort;
import com.dev.phosell.Authentication.Application.ports.out.RegisterUserPort;
import com.dev.phosell.User.domain.exception.UserExistsException;
import com.dev.phosell.User.domain.models.Role;
import com.dev.phosell.User.domain.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterClientService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoadUserPort loadUserPort;
    private  final RegisterUserPort registerUserPort;

    public RegisterClientService(
            BCryptPasswordEncoder passwordEncoder,
            LoadUserPort loadUserPort,
            RegisterUserPort registerUserPort
    ){
        this.passwordEncoder = passwordEncoder;
        this.loadUserPort = loadUserPort;
        this.registerUserPort = registerUserPort;
    }

    public User RegisterClient(User user){

        Optional<User> foundUser = loadUserPort.findByEmail(user.getEmail());

        if(foundUser.isPresent()){
            throw new UserExistsException(foundUser.get().getEmail());
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(hashedPassword);

        user.setRole(Role.CLIENT);

        User savedUser = registerUserPort.save(user);
        return savedUser;
    }
}
