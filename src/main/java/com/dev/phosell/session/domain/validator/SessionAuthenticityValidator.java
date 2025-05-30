package com.dev.phosell.session.domain.validator;

import com.dev.phosell.session.domain.exception.session.SessionOwnershipException;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class SessionAuthenticityValidator {

    public  SessionAuthenticityValidator(){}

    /**
     * Compare user with photographer in session
     *
     * @param session session object to compare.
     * @param user user object to compare.
     * */
    public void validatePhotographerAssignment(Session session, User user)
    {
        if(user.getRole().equals(Role.ADMIN)){
            return;
        }

        if(!session.getPhotographer().getId().equals(user.getId()))
        {
            throw new SessionOwnershipException();

        }

    }
    /**
     *  Compare user with client in session
     *
     * @param session session object to compare.
     * @param user user object to compare.
     * */
    public void validateOwnerShip(Session session, User user)
    {
        if(user.getRole().equals(Role.ADMIN)){
            return;
        }

        if(!session.getClient().getId().equals(user.getId())){
            throw new SessionOwnershipException();
        }
    }
}
