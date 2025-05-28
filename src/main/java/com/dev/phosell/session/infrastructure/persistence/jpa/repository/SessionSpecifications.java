package com.dev.phosell.session.infrastructure.persistence.jpa.repository;

import com.dev.phosell.session.infrastructure.persistence.jpa.entity.SessionEntity;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.util.UUID;

public class SessionSpecifications {

    public static Specification<SessionEntity> byPhotographer(UUID photographerId) {

        return (root, query, cb) -> {

            if(photographerId == null){
                return cb.conjunction();
            }

           return cb.equal(root.get("photographer").get("id"), photographerId);
        };
    }

    public static Specification<SessionEntity> byDate(LocalDate date){

        return (root, query, cb) -> {

            if(date == null){
                return cb.conjunction();
            }

            return cb.equal(root.get("sessionDate"),date);
        };
    }

}