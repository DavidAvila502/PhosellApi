package com.dev.phosell.user.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhotographerResponseDto {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String curp;
}
