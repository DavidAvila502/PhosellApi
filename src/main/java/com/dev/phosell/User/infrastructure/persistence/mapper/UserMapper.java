package com.dev.phosell.User.infrastructure.persistence.mapper;

import com.dev.phosell.User.domain.models.User;
import com.dev.phosell.User.infrastructure.persistence.jpa.entities.UserEntity;
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
                user.getRole()
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
                null,
                null
        );
    }

}
