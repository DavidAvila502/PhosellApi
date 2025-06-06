package com.dev.phosell.session.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PhotographerBasicInfoDto {
    private UUID id;
    private String fullName;
    private String phone;
}
