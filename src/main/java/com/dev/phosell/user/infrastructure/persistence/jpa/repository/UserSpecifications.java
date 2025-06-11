package com.dev.phosell.user.infrastructure.persistence.jpa.repository;

import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.infrastructure.persistence.jpa.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

public class UserSpecifications
{
    public static Specification<UserEntity> byUserId(UUID userId){
        return (root, query, cb) -> {
            if(userId == null){
                return cb.conjunction();
            }

            return cb.equal(root.get("id"),userId);

        };
    }

    public static Specification<UserEntity> byEmail(String email)
    {
        return (root, query, cb) -> {
            if (email == null || email.trim().isEmpty()) {
                return cb.conjunction();
            }

            return cb.equal(root.get("email"),email);
        };
    }

    public static Specification<UserEntity> byFullName(String fullName)
    {
        return (root, query, cb) ->{
          if(fullName == null || fullName.trim().isEmpty()){
              return cb.conjunction();
          }

            return cb.like(
                    cb.lower(root.get("fullName")),
                    "%"+fullName.toLowerCase()+"%"
            );
        };
    }

    public static  Specification<UserEntity> byPhone(String phone)
    {
        return (root, query, cb) ->
        {
            if(phone == null || phone.trim().isEmpty()){
                return cb.conjunction();
            }

            return cb.equal(root.get("phone"),phone);
        };
    }

    public static Specification<UserEntity> byCity(String city)
    {
        return (root, query, cb) ->
        {
            if(city == null || city.trim().isEmpty()){
                return cb.conjunction();
            }

            return cb.like(
                    cb.lower(root.get("city")),
                    "%"+city.toLowerCase()+"%"
            );
        };
    }

    public static Specification<UserEntity> byRole(Role role){

        return (root, query, cb) ->
        {
          if(role == null){
              return cb.conjunction();
          }

          return cb.equal(root.get("role"),role);

        };
    }

    public static  Specification<UserEntity> byIsInService(Boolean isInService)
    {
        return (root, query, cb) ->
        {
            if(isInService == null){
                return cb.conjunction();
            }

            return cb.equal(root.get("isInService"),isInService);
        };
    }
}
