package com.dev.phosell.session.domain.service;
import com.dev.phosell.session.domain.exception.photographer.NotAvailablePhotographerException;
import com.dev.phosell.user.domain.model.User;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ChooseRandomPhotographer {

    public ChooseRandomPhotographer(){}

    public User choosePhotographer(List<User> photographers){

        if (photographers == null || photographers.isEmpty()) {
            throw new NotAvailablePhotographerException();
        }
            int photographerIndex = ThreadLocalRandom.current().nextInt(photographers.size());

        return photographers.get(photographerIndex);
    }
}
