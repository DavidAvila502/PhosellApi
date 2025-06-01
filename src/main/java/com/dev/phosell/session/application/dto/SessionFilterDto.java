package com.dev.phosell.session.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionFilterDto {
    private UUID clientId;
    private UUID photographerId;
    private LocalDate date;
}
