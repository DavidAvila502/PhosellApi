package com.dev.phosell.user.infrastructure.persistence.jpa.repository;

import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.infrastructure.persistence.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
        Optional<UserEntity> findByEmail(String email);
        List<UserEntity> findByRole(Role role);
        List<UserEntity> findByRoleAndIsInService(Role role, boolean inService);
        default List<UserEntity> findPhotographersByIsInService( Boolean isInService){
                return findByRoleAndIsInService(Role.PHOTOGRAPHER,isInService);
        };
}
