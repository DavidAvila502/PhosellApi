package com.dev.phosell.authentication.application.service;

import com.dev.phosell.user.application.port.out.FindUserByEmailPort;
import com.dev.phosell.user.application.port.out.RegisterUserPort;
import com.dev.phosell.user.domain.exception.UserExistsException;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterClientService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final FindUserByEmailPort findUserByEmailPort;
    private  final RegisterUserPort registerUserPort;

    public RegisterClientService(
            BCryptPasswordEncoder passwordEncoder,
            FindUserByEmailPort findUserByEmailPort,
            RegisterUserPort registerUserPort
    ){
        this.passwordEncoder = passwordEncoder;
        this.findUserByEmailPort = findUserByEmailPort;
        this.registerUserPort = registerUserPort;
    }

    public User RegisterClient(User user){

        Optional<User> foundUser = findUserByEmailPort.findByEmail(user.getEmail());

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
