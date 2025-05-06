package com.dev.phosell.Authentication.domain.models;

import com.dev.phosell.User.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefreshToken {
    private UUID id;
    private String token;
    private User user;
    private Timestamp expiryDate;
}
