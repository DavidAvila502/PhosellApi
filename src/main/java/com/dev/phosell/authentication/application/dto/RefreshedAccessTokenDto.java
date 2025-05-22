package com.dev.phosell.authentication.application.dto;

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
