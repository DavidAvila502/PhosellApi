package com.dev.phosell.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ClientResponseDto {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private String city;


}
