package com.dev.phosell.session.application.service;

import com.dev.phosell.session.application.dto.SessionResponseDto;
import com.dev.phosell.session.application.dto.SessionUpdateBasicInfoDto;
import com.dev.phosell.session.application.mapper.SessionDtoMapper;
import com.dev.phosell.session.domain.model.Session;
import com.dev.phosell.session.domain.port.SessionPersistencePort;
import com.dev.phosell.session.domain.service.UpdateSessionBasicInfo;
import com.dev.phosell.session.infrastructure.exception.SessionNotFoundException;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UpdateSessionBasicInfoService {

    private final SessionPersistencePort sessionPersistencePort;
    private final UpdateSessionBasicInfo updateSessionBasicInfo;
    private final SessionDtoMapper sessionDtoMapper;

    public UpdateSessionBasicInfoService(
            SessionPersistencePort sessionPersistencePort,
            UpdateSessionBasicInfo updateSessionBasicInfo,
            SessionDtoMapper sessionDtoMapper
    )
    {
        this.sessionPersistencePort = sessionPersistencePort;
        this.updateSessionBasicInfo = updateSessionBasicInfo;
        this.sessionDtoMapper = sessionDtoMapper;
    }

    public SessionResponseDto updateBasic(SessionUpdateBasicInfoDto updateBasicInfoDto, UUID sessionId){

        Session session = sessionPersistencePort.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("id",sessionId.toString()));

        Session updatedSessionObject = updateSessionBasicInfo.updateInfo(session, updateBasicInfoDto);

        Session updatedSession = sessionPersistencePort.save(updatedSessionObject);

        return sessionDtoMapper.toSessionResponseDto(updatedSession);
    }
}
