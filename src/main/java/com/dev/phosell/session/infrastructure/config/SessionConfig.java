package com.dev.phosell.session.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "sessions")
public class SessionConfig {
    private int earliestBookingHour;
    private int latestBookingHour;
    private int earliestStartWorkingHour;
    private int latestStartWorkingHour;
    private int advanceHours;
    private int duration;
}
