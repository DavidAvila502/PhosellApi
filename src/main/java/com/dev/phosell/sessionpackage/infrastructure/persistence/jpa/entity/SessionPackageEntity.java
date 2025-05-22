package com.dev.phosell.sessionpackage.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionPackageEntity {
    @Id
    @Column(name = "id",updatable = false)
    private UUID id;

    @Column(nullable = false,unique = true)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer photo_count;

    private String benefits;
}
