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

    @NotNull(message = "The fullName can't be null")
    @NotBlank(message = "The fullName can't be blank")
    private String fullName;

    @NotNull(message = "The email can't be null")
    @Email(message = "The email is invalid")
    private String email;

    @NotNull(message = "The password can't be null")
    @NotBlank(message = "The password can't be blank")
    private String password;

    @NotNull(message = "The phone can't be null")
    @NotBlank(message = "The phone can't be blank")
    private String phone;

    @NotNull(message = "The city can't be null")
    @NotBlank(message = "The city can't be blank")
    private String city;
}
