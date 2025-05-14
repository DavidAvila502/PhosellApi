package com.dev.phosell.session.infrastructure.persistence.mapper;

import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.infrastructure.dto.ClientSessionDto;
import com.dev.phosell.session.infrastructure.dto.PhotographerSessionDto;
import com.dev.phosell.session.infrastructure.dto.SessionResponseDto;
import com.dev.phosell.session.infrastructure.persistence.jpa.entity.SessionEntity;
import com.dev.phosell.sessionpackage.infrastructure.persistence.mapper.SessionPackageMapper;
import com.dev.phosell.user.domain.model.User;
import com.dev.phosell.user.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {
    private final SessionPackageMapper sessionPackageMapper;
    private final UserMapper userMapper;

    public SessionMapper(
            SessionPackageMapper sessionPackageMapper,
            UserMapper userMapper
    ){
                this.sessionPackageMapper = sessionPackageMapper;
                this.userMapper = userMapper;
    }

    public Session toDomain(SessionEntity sessionEntity){
        return new Session(
                sessionEntity.getId(),
                userMapper.toDomain(sessionEntity.getClient()),
                userMapper.toDomain(sessionEntity.getPhotographer()),
                sessionPackageMapper.toDomain(sessionEntity.getSessionPackage()),
                sessionEntity.getSessionDate(),
                sessionEntity.getSessionTime(),
                sessionEntity.getLocation(),
                sessionEntity.getSessionStatus(),
                sessionEntity.getPhotosLink(),
                sessionEntity.getCancelReason()
        );
    }

   public SessionEntity toEntity(Session session){
        return new SessionEntity(
                session.getId(),
                userMapper.toEntity(session.getClient()),
                userMapper.toEntity(session.getPhotographer()),
                sessionPackageMapper.toEntity(session.getSessionPackage()),
                session.getSessionDate(),
                session.getSessionTime(),
                session.getLocation(),
                session.getSessionStatus(),
                session.getPhotosLink(),
                session.getCancelReason()
        );
    }

    public SessionResponseDto toSessionResponseDto(Session session){

        // Get the client
        User client = session.getClient();

        ClientSessionDto clientSessionDto =
                new ClientSessionDto(client.getId(),client.getFullName(), client.getPhone());

        // Get the photographer
        User photographer = session.getPhotographer();

        PhotographerSessionDto photographerSessionDto =
                new PhotographerSessionDto(photographer.getId(), photographer.getFullName(), photographer.getPhone());

        // return
        return new SessionResponseDto(
                session.getId(),
                clientSessionDto,
                photographerSessionDto,
                session.getSessionPackage(),
                session.getSessionDate(),
                session.getSessionTime(),
                session.getLocation(),
                session.getSessionStatus(),
                session.getPhotosLink(),
                session.getCancelReason()
        );
    }

}
