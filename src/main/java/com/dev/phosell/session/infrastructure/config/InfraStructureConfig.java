package com.dev.phosell.session.infrastructure.config;

import com.dev.phosell.session.domain.service.ChooseRandomPhotographer;
import com.dev.phosell.session.domain.service.GenerateSessionSlots;
import com.dev.phosell.session.domain.service.SessionSlotsAvailabilityCalculator;
import com.dev.phosell.session.domain.validator.SessionBookingPolicyValidator;
import com.dev.phosell.session.domain.validator.SlotGenerationValidator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SessionConfig.class)
public class InfraStructureConfig {

    @Bean
    public SessionSlotsAvailabilityCalculator SessionSlotsAvailabilityCalculator(SessionConfig sessionConfig){
        return new SessionSlotsAvailabilityCalculator(sessionConfig.getDuration());
    }

    @Bean
    public SlotGenerationValidator slotDomainValidator(SessionConfig sessionConfig){
        return  new SlotGenerationValidator(
                sessionConfig.getEarliestBookingHour(),
                sessionConfig.getLatestBookingHour(),
                sessionConfig.getLatestStartWorkingHour(),
                sessionConfig.getEarliestStartWorkingHour(),
                sessionConfig.getAdvanceHours(),
                sessionConfig.getDuration()
        );
    }

    @Bean public GenerateSessionSlots generateSessionSlots(SessionConfig sessionConfig, SlotGenerationValidator slotGenerationValidator){
        return new GenerateSessionSlots(sessionConfig , slotGenerationValidator);
    }

    @Bean public SessionBookingPolicyValidator sessionBookingPolicyValidator(SessionConfig sessionConfig){
        return new SessionBookingPolicyValidator(
                sessionConfig.getEarliestBookingHour(),
                sessionConfig.getLatestBookingHour(),
                sessionConfig.getEarliestStartWorkingHour(),
                sessionConfig.getLatestStartWorkingHour(),
                sessionConfig.getAdvanceHours(),
                sessionConfig.getDuration()
        );
    }

    @Bean public ChooseRandomPhotographer chooseRandomPhotographer(){
        return  new ChooseRandomPhotographer();
    }
}
