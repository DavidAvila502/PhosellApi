package com.dev.phosell.session.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClientSessionDto {
    private UUID id;
    private String fullName;
    private String phone;
}
