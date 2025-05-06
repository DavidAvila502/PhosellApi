package com.dev.phosell.authentication.infrastructure.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String userName;
    private String jwtToken;
    private Long expiresIn;

    public  LoginResponseDto(){}

    public LoginResponseDto(String userName, String jwtToken, Long expiresIn) {
        this.userName = userName;
        this.jwtToken = jwtToken;
        this.expiresIn = expiresIn;
    }
}
