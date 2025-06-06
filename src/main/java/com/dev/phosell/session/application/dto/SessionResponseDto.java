package com.dev.phosell.session.application.dto;

import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionResponseDto {
    private UUID id;
    private ClientBasicInfoDto client;
    private PhotographerBasicInfoDto photographer;
    private SessionPackage sessionPackage;
    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private String location;
    private SessionStatus sessionStatus;
    private String photosLink;
    private String cancelReason;
}
