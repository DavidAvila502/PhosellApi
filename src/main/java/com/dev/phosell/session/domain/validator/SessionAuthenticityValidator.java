package com.dev.phosell.session.domain.validator;

import com.dev.phosell.session.domain.exception.session.SessionOwnershipException;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class SessionAuthenticityValidator {

    public  SessionAuthenticityValidator(){}

    public void validatePhotographerAssignment(Session session, User authUser)
    {
        if(authUser.getRole().equals(Role.ADMIN)){
            return;
        }

        if(!session.getPhotographer().getId().equals(authUser.getId()))
        {
            throw new SessionOwnershipException();

        }

    }

    public void validateOwnerShip(Session session, User authUser)
    {
        if(authUser.getRole().equals(Role.ADMIN)){
            return;
        }

        if(!session.getClient().getId().equals(authUser.getId())){
            throw new SessionOwnershipException();
        }
    }

}
