package com.dev.phosell.user.infrastructure.persistence.jpa.entity;

import com.dev.phosell.user.domain.model.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data // Incluye getters/setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @Column(name = "id", updatable = false)
    private UUID id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    // Solo requerido para CLIENT
    private String city;

    // Solo requerido para PHOTOGRAPHER
    private String address;

    // Solo requerido para PHOTOGRAPHER
    private String curp;

    // Solo requerido para PHOTOGRAPHER (URL de imagen)
    @Column(name = "id_photo_front")
    private String idPhotoFront;

    // Solo requerido para PHOTOGRAPHER (URL de imagen)
    @Column(name = "id_photo_back")
    private String idPhotoBack;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "is_in_service")
    private Boolean isInService;

    @Column(name = "created_by")
    private UUID createdById;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
