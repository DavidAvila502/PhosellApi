package com.dev.phosell.session.infrastructure.persistence.jpa.entity;

import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.sessionpackage.infrastructure.persistence.jpa.entity.SessionPackageEntity;
import com.dev.phosell.user.infrastructure.persistence.jpa.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionEntity {

    public SessionEntity(
            UUID id,
            UserEntity client,
            UserEntity photographer,
            SessionPackageEntity sessionPackage,
            LocalDate sessionDate,
            LocalTime sessionTime,
            String location,
            SessionStatus sessionStatus,
            String photosLink,
            String cancelReason,
            Timestamp cancelledAt
    ) {
        this.id = id;
        this.client = client;
        this.photographer = photographer;
        this.sessionPackage = sessionPackage;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.location = location;
        this.sessionStatus = sessionStatus;
        this.photosLink = photosLink;
        this.cancelReason = cancelReason;
        this.cancelledAt = cancelledAt;
    }

    @Id
    @Column(name = "id", updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id",nullable = false)
    private UserEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id")
    private UserEntity photographer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "package_id", nullable = false)
    private SessionPackageEntity sessionPackage;

    @Column(name = "session_date",nullable = false)
    private LocalDate sessionDate;

    @Column(name = "session_time", nullable = false)
    private LocalTime sessionTime;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus sessionStatus;

    @Column(name = "photos_link")
    private String photosLink;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "cancelled_at")
    private Timestamp cancelledAt;

    @CreationTimestamp
    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

}
