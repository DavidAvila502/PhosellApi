package com.dev.phosell.User.infrastructure.adapters.out;

import com.dev.phosell.Authentication.Application.ports.out.LoadUserPort;
import com.dev.phosell.Authentication.Application.ports.out.RegisterUserPort;
import com.dev.phosell.User.application.ports.UserPersistencePort;
import com.dev.phosell.User.domain.models.Role;
import com.dev.phosell.User.domain.models.User;
import com.dev.phosell.User.infrastructure.persistence.jpa.entities.UserEntity;
import com.dev.phosell.User.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.dev.phosell.User.infrastructure.persistence.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class UserJpaAdapter implements UserPersistencePort , LoadUserPort, RegisterUserPort {

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
}
