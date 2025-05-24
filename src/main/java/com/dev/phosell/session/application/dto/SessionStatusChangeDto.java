package com.dev.phosell.session.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionStatusChangeDto {
    private String newSessionStatus;
}
