package com.dev.phosell.session.domain.validator;

import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.user.domain.exception.InvalidUserValueException;
import java.util.List;

public class SessionSwapPhotographerValidator {

    public SessionSwapPhotographerValidator(){}

    public void validate(Session sessionA, Session sessionB)
    {
        validateSlot(sessionA,sessionB);
        validateStatus(sessionA,sessionB);
    }

    private void validateSlot(Session sessionA, Session sessionB)
    {
        if(!sessionA.getSessionDate().equals(sessionB.getSessionDate()))
        {
            throw new InvalidUserValueException("sessionDate",
                    sessionA.getSessionDate().toString(),"Is not the same date that "+sessionB.getSessionDate());
        }

        if(!sessionA.getSessionTime().equals(sessionB.getSessionTime()))
        {
            throw new InvalidUserValueException("sessionTime",
                    sessionA.getSessionTime().toString(),"Is not the same time that "+ sessionB.getSessionTime());
        }
    }

    private void validateStatus(Session sessionA, Session sessionB)
    {
        List<SessionStatus> allowedStatuses =
                List.of(SessionStatus.REQUESTED,SessionStatus.CONFIRMED,SessionStatus.IN_PROGRESS);

        if(!allowedStatuses.contains(sessionA.getSessionStatus())){
            throw new InvalidUserValueException("sessionStatus",
                    sessionA.getSessionStatus().toString(),"Is not a valid status to swap photographer");
        }

        if(!allowedStatuses.contains(sessionB.getSessionStatus())){
            throw new InvalidUserValueException("sessionStatus",
                    sessionB.getSessionStatus().toString(),"Is not a valid status to swap photographer");
        }
    }
}
