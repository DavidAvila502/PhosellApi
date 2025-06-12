package com.dev.phosell.user.application.dto;

import com.dev.phosell.user.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserFiltersDto {
    private UUID id;
    private String email;
    private String name;
    private String phone;
    private String city;
    private Role role;
    private Boolean isInService;

}
