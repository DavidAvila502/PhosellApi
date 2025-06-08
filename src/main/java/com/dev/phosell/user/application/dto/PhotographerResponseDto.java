package com.dev.phosell.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@AllArgsConstructor
@Data
public class PhotographerResponseDto {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String curp;
    private boolean isInService;
}
