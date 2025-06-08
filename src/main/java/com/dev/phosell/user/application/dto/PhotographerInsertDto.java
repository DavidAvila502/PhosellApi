package com.dev.phosell.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotographerInsertDto {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String fullAddress;
    private String curp;

    // -- Optional values
    private String city;
}
