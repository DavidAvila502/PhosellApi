package com.dev.phosell.sessionpackage.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionPackage {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer photo_count;
    private String benefits;
}
