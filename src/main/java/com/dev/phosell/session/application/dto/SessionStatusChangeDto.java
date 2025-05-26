package com.dev.phosell.session.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionStatusChangeDto {
    @NotNull(message = "newSessionStatus can't be null")
    @NotBlank(message = "newSessionStatus can't be blank")
    private String newSessionStatus;
}
