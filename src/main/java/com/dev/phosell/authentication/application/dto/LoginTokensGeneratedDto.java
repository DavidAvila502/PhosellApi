package com.dev.phosell.authentication.application.dto;

import com.dev.phosell.authentication.domain.model.RefreshToken;
import com.dev.phosell.user.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginTokensGeneratedDto {
    private UUID userId;
    private String fullName;
    private String email;
    private Role role;
    private String accessToken;
    private Long accessTokenExpiresIn;
    private RefreshToken refreshToken;
}
