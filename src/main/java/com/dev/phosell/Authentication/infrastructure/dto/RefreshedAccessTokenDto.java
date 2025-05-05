package com.dev.phosell.Authentication.infrastructure.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshedAccessTokenDto {
    private String jwtToken;
    private Long expiresIn;

}
