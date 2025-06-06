package com.dev.phosell.session.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionFilterDto {
    private LocalDate date;
    private LocalTime time;
    private UUID photographerId;
    private UUID clientId;
    private List<String> statuses;
}
