package com.dev.phosell.session.domain.model;

import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import com.dev.phosell.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Session {
    private UUID id;
    private User client;
    private User photographer;
    private SessionPackage sessionPackage;
    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private String location;
    private SessionStatus sessionStatus;
    private String photosLink;
    private String cancelReason;
}
