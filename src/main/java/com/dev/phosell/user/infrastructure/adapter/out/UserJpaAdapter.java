package com.dev.phosell.user.infrastructure.adapter.out;

import com.dev.phosell.user.application.port.out.*;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.infrastructure.persistence.jpa.entity.UserEntity;
import com.dev.phosell.user.infrastructure.persistence.jpa.repository.UserJpaRepository;
import com.dev.phosell.user.infrastructure.persistence.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class UserJpaAdapter implements
        UserPersistencePort , FindUserByEmailPort,
        RegisterUserPort, FindPhotographersByIsInServicePort, FindUserByIdPort {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;
    @Autowired
    public UserJpaAdapter(UserJpaRepository userJpaRepository, UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserEntity newUser = userMapper.toEntity(user);

        UserEntity savedUser = userJpaRepository.save(newUser);

        return userMapper.toDomain(savedUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {

        Optional<UserEntity> user = userJpaRepository.findByEmail(email);
        return user.map(userMapper::toDomain);

    }

    @Override
    public List<User> findByRole(Role role) {
        return userJpaRepository.findByRole(role).stream().map(userMapper::toDomain).toList();
    }

    @Override
    public List<User> findPhotographersByIsInService(Boolean isInService) {
        return userJpaRepository.findPhotographersByIsInService(isInService)
                .stream().map(u -> userMapper.toDomain(u)).toList();
    }

    @Override
    public Optional<User> findById(UUID id) {
        Optional<UserEntity> user = userJpaRepository.findById(id);
        return user.map(u-> userMapper.toDomain(u));
    }
}
