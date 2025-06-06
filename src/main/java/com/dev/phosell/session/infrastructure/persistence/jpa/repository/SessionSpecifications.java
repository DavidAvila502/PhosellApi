package com.dev.phosell.session.infrastructure.persistence.jpa.repository;

import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.session.infrastructure.persistence.jpa.entity.SessionEntity;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
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

    public static Specification<SessionEntity> byClient(UUID clientId)
    {
        return (root, query, cb) -> {
            if(clientId == null){
                return cb.conjunction();
            }

            return cb.equal(root.get("client").get("id"),clientId);
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

    public static Specification<SessionEntity> byTime(LocalTime time){

        return (root, query, cb) ->{
           if(time == null){
               return cb.conjunction();
           }

           return cb.equal(root.get("sessionTime"),time);
        } ;

    }

    public static Specification<SessionEntity> byStatusIn(Collection<SessionStatus> statuses){

        return (root, query, cb) ->{
            if(statuses == null){
                return cb.conjunction();
            }

            return root.get("sessionStatus").in(statuses);

        };
    }

}