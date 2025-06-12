package com.dev.phosell.session.application.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SessionUpdateBasicInfoDto {
    @NotNull
    @NotBlank
    private String location;
    private String photosLink;
    private String cancelReason;
    private LocalDateTime cancelledAt;
}
