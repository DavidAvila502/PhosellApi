package com.dev.phosell.session.application.mapper;

import com.dev.phosell.session.application.dto.ClientBasicInfoDto;
import com.dev.phosell.session.application.dto.PhotographerBasicInfoDto;
import com.dev.phosell.session.application.dto.SessionResponseDto;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class SessionDtoMapper {

    public SessionResponseDto toSessionResponseDto(Session session){

        // Get the client
        User client = session.getClient();

        ClientBasicInfoDto clientBasicInfoDto =
                new ClientBasicInfoDto(client.getId(),client.getFullName(), client.getPhone());

        // Get the photographer
        User photographer = session.getPhotographer();

        PhotographerBasicInfoDto photographerBasicInfoDto =
                new PhotographerBasicInfoDto(photographer.getId(), photographer.getFullName(), photographer.getPhone());

        // return
        return new SessionResponseDto(
                session.getId(),
                clientBasicInfoDto,
                photographerBasicInfoDto,
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
