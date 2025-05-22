package com.dev.phosell.authentication.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterClientDto {

    @NotNull
    @NotBlank
    private String fullName;
    @NotNull
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String password;
    @NotNull
    @NotBlank
    private String phone;
    @NotNull
    @NotBlank
    private String city;
}
