package com.dev.phosell.authentication.application.dto;

import com.dev.phosell.authentication.domain.model.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginTokensGeneratedDto {
    private String userName;
    private String accessToken;
    private Long accessTokenExpiresIn;
    private RefreshToken refreshToken;
}
