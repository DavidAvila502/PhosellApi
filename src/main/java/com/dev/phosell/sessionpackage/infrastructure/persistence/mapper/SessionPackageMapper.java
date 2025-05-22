package com.dev.phosell.sessionpackage.infrastructure.persistence.mapper;

import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import com.dev.phosell.sessionpackage.infrastructure.persistence.jpa.entity.SessionPackageEntity;
import org.springframework.stereotype.Component;

@Component
public class SessionPackageMapper {

    public  SessionPackageMapper(){}


    public SessionPackage toDomain(SessionPackageEntity sessionPackageEntity){

        return new SessionPackage(
                sessionPackageEntity.getId(),
                sessionPackageEntity.getName(),
                sessionPackageEntity.getDescription(),
                sessionPackageEntity.getPrice(),
                sessionPackageEntity.getPhoto_count(),
                sessionPackageEntity.getBenefits()
        );

    }

    public SessionPackageEntity toEntity(SessionPackage sessionPackage){
        return  new SessionPackageEntity(
                sessionPackage.getId(),
                sessionPackage.getName(),
                sessionPackage.getDescription(),
                sessionPackage.getPrice(),
                sessionPackage.getPhoto_count(),
                sessionPackage.getBenefits()
        );

    }

}
