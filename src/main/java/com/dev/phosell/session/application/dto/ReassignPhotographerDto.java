package com.dev.phosell.session.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReassignPhotographerDto {
    @NotNull(message = "The newPhotographerId is required")
    private UUID newPhotographerId;
}
