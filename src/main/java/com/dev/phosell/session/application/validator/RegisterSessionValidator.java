package com.dev.phosell.session.application.validator;

import com.dev.phosell.sessionpackage.application.port.out.FindSessionPackageByIdPort;
import com.dev.phosell.sessionpackage.domain.exception.SessionPackageNotFoundExcaption;
import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import com.dev.phosell.user.application.port.out.FindUserByIdPort;
import com.dev.phosell.user.domain.exception.UserNotFoundException;
import com.dev.phosell.user.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegisterSessionValidator {

    private final FindUserByIdPort findUserByIdPort;
    private final FindSessionPackageByIdPort findSessionPackageByIdPort;

    public  RegisterSessionValidator(
            FindUserByIdPort findUserByIdPort,
            FindSessionPackageByIdPort findSessionPackageByIdPort
    ){

        this.findUserByIdPort = findUserByIdPort;
        this.findSessionPackageByIdPort =findSessionPackageByIdPort;
    }

    public User validateClient(UUID id){
        return  findUserByIdPort.findById(id).orElseThrow(() -> new UserNotFoundException("id",id.toString()));
    }

    public User validatePhotographer(UUID id){
        return findUserByIdPort.findById(id).orElseThrow(() -> new UserNotFoundException("id",id.toString()));
    }

    public SessionPackage validateSessionPackage(UUID id){
        return findSessionPackageByIdPort.findById(id).orElseThrow(() -> new SessionPackageNotFoundExcaption("id",id.toString()));
    }
}
