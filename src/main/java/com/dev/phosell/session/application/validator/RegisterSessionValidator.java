package com.dev.phosell.session.application.validator;

import com.dev.phosell.sessionpackage.application.exception.SessionPackageNotFoundExcaption;
import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import com.dev.phosell.sessionpackage.domain.port.SessionPackagePersistencePort;
import com.dev.phosell.user.application.exception.UserNotFoundException;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.domain.port.UserPersistencePort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegisterSessionValidator {

    private final UserPersistencePort userPersistencePort;
    private final SessionPackagePersistencePort sessionPackagePersistencePort;

    public  RegisterSessionValidator(
            UserPersistencePort userPersistencePort,
            SessionPackagePersistencePort sessionPackagePersistencePort
    ){
        this.userPersistencePort = userPersistencePort;
        this.sessionPackagePersistencePort = sessionPackagePersistencePort;
    }

    public User validateClient(UUID id){
        return  userPersistencePort.findById(id).orElseThrow(() -> new UserNotFoundException("id",id.toString()));
    }

    public User validatePhotographer(UUID id){
        return userPersistencePort.findById(id).orElseThrow(() -> new UserNotFoundException("id",id.toString()));
    }

    public SessionPackage validateSessionPackage(UUID id){
        return sessionPackagePersistencePort.findById(id).orElseThrow(() -> new SessionPackageNotFoundExcaption("id",id.toString()));
    }
}
