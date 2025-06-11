package com.dev.phosell.user.infrastructure.adapter.out;

import com.dev.phosell.user.application.dto.UserFiltersDto;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.domain.port.*;
import com.dev.phosell.user.infrastructure.persistence.jpa.entity.UserEntity;
import com.dev.phosell.user.infrastructure.persistence.jpa.repository.UserJpaRepository;
import com.dev.phosell.user.infrastructure.persistence.jpa.repository.UserSpecifications;
import com.dev.phosell.user.infrastructure.persistence.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class UserJpaAdapter implements UserPersistencePort
{

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
    public Page<User> findByFilters(UserFiltersDto filters, Pageable pageable) {
        Specification<UserEntity> userSpecifications = Specification
                .where(UserSpecifications.byUserId(filters.getId()))
                .and(UserSpecifications.byEmail(filters.getEmail()))
                .and(UserSpecifications.byFullName(filters.getName()))
                .and(UserSpecifications.byPhone(filters.getPhone()))
                .and(UserSpecifications.byCity(filters.getCity()))
                .and(UserSpecifications.byRole(filters.getRole()))
                .and(UserSpecifications.byIsInService(filters.getIsInService()))
                ;

        return userJpaRepository.findAll(userSpecifications,pageable)
                .map(userEntity -> userMapper.toDomain(userEntity));
    }

    @Override
    public Optional<User> findById(UUID id) {
        Optional<UserEntity> user = userJpaRepository.findById(id);
        return user.map(u-> userMapper.toDomain(u));
    }
}
