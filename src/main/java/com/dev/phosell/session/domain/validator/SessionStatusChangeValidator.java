package com.dev.phosell.session.domain.validator;

import com.dev.phosell.session.domain.model.SessionStatus;
import com.dev.phosell.user.domain.model.Role;
import org.springframework.security.authorization.AuthorizationDeniedException;
import java.util.*;

public class SessionStatusChangeValidator {

    private EnumMap<SessionStatus, EnumMap<Role, EnumSet<SessionStatus>>> rules;

    public SessionStatusChangeValidator(){
        this.loadStatusesAndRoleRules();
    }

    public void validateStatusAndRolePermissions(SessionStatus currentStatus, SessionStatus newStatus,Role userRole){

        EnumMap<Role, EnumSet<SessionStatus>> roleMap =
                rules.getOrDefault(currentStatus, new EnumMap<>(Role.class));

        EnumSet<SessionStatus> allowedStatuses =
                roleMap.getOrDefault(userRole, EnumSet.noneOf(SessionStatus.class));

        if (!allowedStatuses.contains(newStatus)) {
            throw new AuthorizationDeniedException(
                    String.format("Role %s cannot transition from %s to %s",
                            userRole, currentStatus, newStatus));
        }
    }

    private void loadStatusesAndRoleRules(){
        rules = new EnumMap<>(SessionStatus.class);

        // requested rules
        EnumMap<Role, EnumSet<SessionStatus>> requestedMap = new EnumMap<>(Role.class);
        requestedMap.put(
                Role.ADMIN,
                EnumSet.of(
                SessionStatus.CONFIRMED,
                SessionStatus.IN_PROGRESS, SessionStatus.PHOTOS_PENDING,
                SessionStatus.COMPLETED, SessionStatus.CANCELLED_BY_ADMIN));

        requestedMap.put(
                Role.CLIENT,
                EnumSet.of(SessionStatus.CANCELLED_BY_CLIENT));

        rules.put(SessionStatus.REQUESTED, requestedMap);

        // Confirmed rules
        EnumMap<Role, EnumSet<SessionStatus>> confirmedMap = new EnumMap<>(Role.class);
        confirmedMap.put(
                Role.ADMIN,
                EnumSet.of(
                        SessionStatus.REQUESTED, SessionStatus.IN_PROGRESS,
                        SessionStatus.PHOTOS_PENDING, SessionStatus.COMPLETED,
                        SessionStatus.CANCELLED_BY_ADMIN));

        confirmedMap.put(
                Role.CLIENT,
                EnumSet.of(
                  SessionStatus.CANCELLED_BY_CLIENT));

        confirmedMap.put(
                Role.PHOTOGRAPHER,
                EnumSet.of(SessionStatus.IN_PROGRESS));

        rules.put(SessionStatus.CONFIRMED,confirmedMap);


        // In progress rules
        EnumMap<Role, EnumSet<SessionStatus>> inProgressMap = new EnumMap<>(Role.class);
        inProgressMap.put(
                Role.ADMIN,
                EnumSet.of(
                    SessionStatus.REQUESTED,SessionStatus.CONFIRMED,
                    SessionStatus.PHOTOS_PENDING, SessionStatus.COMPLETED,
                        SessionStatus.CANCELLED_BY_ADMIN));

        inProgressMap.put(
                Role.PHOTOGRAPHER,
                EnumSet.of(SessionStatus.PHOTOS_PENDING));

        inProgressMap.put(
                Role.CLIENT,
                EnumSet.of(SessionStatus.CANCELLED_BY_CLIENT)
        );

        rules.put(SessionStatus.IN_PROGRESS,inProgressMap);

        //Photos pending rules
        EnumMap<Role, EnumSet<SessionStatus>> photosPendingMap = new EnumMap<>(Role.class);
        photosPendingMap.put(
                Role.ADMIN,
                EnumSet.of(
                        SessionStatus.REQUESTED,SessionStatus.CONFIRMED,
                        SessionStatus.IN_PROGRESS, SessionStatus.COMPLETED,
                        SessionStatus.CANCELLED_BY_ADMIN));

        photosPendingMap.put(
                Role.PHOTOGRAPHER,
                EnumSet.of(
                        SessionStatus.COMPLETED
                )
        );

        rules.put(SessionStatus.PHOTOS_PENDING,photosPendingMap);

        //completed rules
        EnumMap<Role, EnumSet<SessionStatus>> completedMap = new EnumMap<>(Role.class);
        completedMap.put(
          Role.ADMIN,
                EnumSet.of(
                        SessionStatus.REQUESTED,SessionStatus.CONFIRMED,
                        SessionStatus.IN_PROGRESS, SessionStatus.PHOTOS_PENDING,
                        SessionStatus.COMPLETED, SessionStatus.CANCELLED_BY_ADMIN));

        rules.put(SessionStatus.COMPLETED,completedMap);

        //Cancel by client rules
        EnumMap<Role, EnumSet<SessionStatus>> cancelByClientMap = new EnumMap<>(Role.class);
        cancelByClientMap.put(
          Role.ADMIN,
          EnumSet.of(SessionStatus.REQUESTED,SessionStatus.CONFIRMED,
                  SessionStatus.IN_PROGRESS, SessionStatus.PHOTOS_PENDING,
                  SessionStatus.COMPLETED,SessionStatus.CANCELLED_BY_ADMIN)
        );

        rules.put(SessionStatus.CANCELLED_BY_CLIENT,cancelByClientMap);

        //Cancel by admin
        EnumMap<Role, EnumSet<SessionStatus>> cancelByAdminMap = new EnumMap<>(Role.class);
        cancelByAdminMap.put(
                Role.ADMIN,
                EnumSet.of(SessionStatus.REQUESTED,SessionStatus.CONFIRMED,
                        SessionStatus.IN_PROGRESS, SessionStatus.PHOTOS_PENDING,
                        SessionStatus.COMPLETED,SessionStatus.CANCELLED_BY_ADMIN)
        );

        rules.put(SessionStatus.CANCELLED_BY_ADMIN,cancelByAdminMap);

    }
}
