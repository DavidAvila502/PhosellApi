package com.dev.phosell.session.application.mapper;

import com.dev.phosell.session.application.dto.ClientSessionDto;
import com.dev.phosell.session.application.dto.PhotographerSessionDto;
import com.dev.phosell.session.application.dto.SessionResponseDto;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class SessionToDtoMapper {

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
