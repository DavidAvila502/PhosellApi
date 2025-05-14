package com.dev.phosell.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UUID id;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String city;
    private String address;
    private String curp;
    private String idPhotoFront;
    private String idPhotoBack;
    private Role role;
    private Boolean isInService;

}
