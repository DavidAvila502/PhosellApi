package com.dev.phosell.session.domain.service;

import com.dev.phosell.session.application.dto.SessionUpdateBasicInfoDto;
import com.dev.phosell.session.domain.model.Session;
import java.util.Objects;

public class UpdateSessionBasicInfo {

    /**
     * Update the basic properties of a session if needed
     *
     * @param session a session to update
     * @param infoDto a dto with the new information
     * @return a session object with the new properties updated
     * */
    public Session updateInfo(Session session, SessionUpdateBasicInfoDto infoDto){

        if(!Objects.equals(infoDto.getLocation(),(session.getLocation())))
        {
            session.setLocation(infoDto.getLocation());
        }

        if(!Objects.equals(infoDto.getCancelReason(),(session.getCancelReason())))
        {
            session.setCancelReason(infoDto.getCancelReason());
        }

        if(!Objects.equals(infoDto.getPhotosLink(),(session.getPhotosLink())))
        {
            session.setPhotosLink(infoDto.getPhotosLink());
        }

        if(!Objects.equals(infoDto.getCancelledAt(),(session.getCancelledAt())))
        {
            session.setCancelledAt(infoDto.getCancelledAt());
        }

        return session;
    }
}
