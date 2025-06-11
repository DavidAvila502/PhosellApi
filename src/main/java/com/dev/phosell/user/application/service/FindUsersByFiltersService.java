package com.dev.phosell.user.application.service;

import com.dev.phosell.user.application.dto.UserFiltersDto;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.domain.port.UserPersistencePort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FindUsersByFiltersService {

    private final UserPersistencePort userPersistencePort;

    public FindUsersByFiltersService(UserPersistencePort userPersistencePort){
        this.userPersistencePort = userPersistencePort;
    }

    public Page<User> findByFilters(UserFiltersDto filtersDto, Pageable pageable){
        return userPersistencePort.findByFilters(filtersDto,pageable);
    }

}
