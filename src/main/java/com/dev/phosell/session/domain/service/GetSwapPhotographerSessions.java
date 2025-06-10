package com.dev.phosell.session.domain.service;

import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.user.domain.model.User;
import java.util.List;

public class GetSwapPhotographerSessions {

    public List<Session> swapPhotographers(Session sessionA, Session sessionB){

        User photographerA = sessionA.getPhotographer();
        User photographerB = sessionB.getPhotographer();

        sessionA.setPhotographer(photographerB);
        sessionB.setPhotographer(photographerA);

        return List.of(sessionA,sessionB);
    }
}
