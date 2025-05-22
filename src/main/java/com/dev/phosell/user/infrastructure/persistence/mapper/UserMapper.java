package com.dev.phosell.user.infrastructure.persistence.mapper;

import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.infrastructure.persistence.jpa.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity user){
        return new User(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                user.getCity(),
                user.getAddress(),
                user.getCurp(),
                user.getIdPhotoFront(),
                user.getIdPhotoBack(),
                user.getRole(),
                user.getIsInService()
        );
    };


    public UserEntity toEntity(User user){
        return new UserEntity(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                user.getCity(),
                user.getAddress(),
                user.getCurp(),
                user.getIdPhotoFront(),
                user.getIdPhotoBack(),
                user.getRole(),
                user.getInService(),
                null,
                null
        );
    }

}
