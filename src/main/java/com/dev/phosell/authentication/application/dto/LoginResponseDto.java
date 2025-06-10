package com.dev.phosell.authentication.application.dto;

import com.dev.phosell.user.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private UUID id;
    private String fullName;
    private String email;
    private Role role;
    private String jwtToken;
    private Long expiresIn;
}
