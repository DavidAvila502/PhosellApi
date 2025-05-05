package com.dev.phosell.Authentication.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterClientResponseDto {

    private UUID id;

    private String fullName;

    private String email;

//    private String password;

    private String phone;

    private String city;
}
