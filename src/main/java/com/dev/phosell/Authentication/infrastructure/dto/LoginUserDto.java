package com.dev.phosell.Authentication.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class LoginUserDto {

    @NonNull
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;

    @NonNull
    @NotBlank(message = "password is required")
    private  String password;

}
