package com.dev.phosell.session.infrastructure.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionInsertDto {

    @NotNull(message = "The clientId can't be null")
    private UUID clientId;

    @NotNull(message = "The sessionPackageId can't be null")
    private UUID sessionPackageId;

    @NotNull(message = "The sessionDate can't be null")
    @FutureOrPresent(message = "The sessionDate must be today or in the future")
    private LocalDate sessionDate;

    @NotNull(message = "The sessionTime can't be null")
    private LocalTime sessionTime;

    @NotBlank(message = "the location can't be blank")
    @Length(min = 15, message = "The location must to have at least 15 characters")
    private String location;
}
