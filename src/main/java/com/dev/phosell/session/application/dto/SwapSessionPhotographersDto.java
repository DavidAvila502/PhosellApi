package com.dev.phosell.session.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SwapSessionPhotographersDto {
    @NotNull(message = "The sessionAId can't be null")
    private UUID sessionAId;
    @NotNull(message = "The sessionBId can't be null")
    private UUID sessionBId;
}
