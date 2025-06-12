package com.dev.phosell.session.infrastructure.config;

import com.dev.phosell.session.domain.service.*;
import com.dev.phosell.session.domain.validator.SessionBookingPolicyValidator;
import com.dev.phosell.session.domain.validator.SessionStatusChangeValidator;
import com.dev.phosell.session.domain.validator.SessionSwapPhotographerValidator;
import com.dev.phosell.session.domain.validator.SlotGenerationValidator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties(SessionConfig.class)
public class InfraStructureConfig {

    @Bean
    public Clock systemClock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public SessionSlotsAvailabilityCalculator SessionSlotsAvailabilityCalculator(SessionConfig sessionConfig){
        return new SessionSlotsAvailabilityCalculator(sessionConfig.getDuration());
    }

    @Bean
    public SlotGenerationValidator slotDomainValidator(SessionConfig sessionConfig, Clock clock){
        return  new SlotGenerationValidator(
                sessionConfig.getEarliestBookingHour(),
                sessionConfig.getLatestBookingHour(),
                sessionConfig.getEarliestStartWorkingHour(),
                sessionConfig.getLatestStartWorkingHour(),
                sessionConfig.getAdvanceHours(),
                sessionConfig.getDuration(),
                clock
        );
    }

    @Bean public GenerateSessionSlots generateSessionSlots(
            SessionConfig sessionConfig,
            SlotGenerationValidator slotGenerationValidator,
            Clock clock
    ){
        return new GenerateSessionSlots(sessionConfig , slotGenerationValidator,clock);
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

    @Bean public SessionStatusChangeValidator sessionStatusChangeValidator(){
        return  new SessionStatusChangeValidator();
    }

    @Bean public SessionSwapPhotographerValidator sessionSwapPhotographerValidator(){
        return new SessionSwapPhotographerValidator();
    }

    @Bean public GetSwapPhotographerSessions getSwapPhotographerSessions(){
        return new GetSwapPhotographerSessions();
    }

    @Bean public UpdateSessionBasicInfo updateSessionBasicInfo(){
        return new UpdateSessionBasicInfo();
    }
}
