package com.dev.phosell.session.domain.service;

import com.dev.phosell.session.domain.validator.SlotGenerationValidator;
import com.dev.phosell.session.infrastructure.config.SessionConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.*;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class GenerateSessionSlotsTest {

    private SessionConfig sessionConfigMock;
    private SlotGenerationValidator validator;
    private Clock fixedClock;
    private GenerateSessionSlots generator;

    @BeforeEach
    void setUp() {
        sessionConfigMock = Mockito.mock(SessionConfig.class);

        when(sessionConfigMock.getDuration()).thenReturn(60);
        when(sessionConfigMock.getEarliestStartWorkingHour()).thenReturn(8);
        when(sessionConfigMock.getLatestStartWorkingHour()).thenReturn(20);
        when(sessionConfigMock.getAdvanceHours()).thenReturn(3);

    }

    @Test
    void get_slots_when_is_today_at_time_14_15() {
        LocalDateTime instant = LocalDateTime.of(2025, Month.JUNE, 2, 14, 15);
        ZoneId zona = ZoneId.systemDefault();
        fixedClock = Clock.fixed( instant.atZone(zona).toInstant(), zona );

        validator = new SlotGenerationValidator(
                6,
                17,
                6,
                20,
                3,
                60,
                fixedClock
        );

        generator = new GenerateSessionSlots(sessionConfigMock, validator, fixedClock);

        LocalDate today = LocalDate.of(2025, Month.JUNE, 2);

        List<LocalTime> result = generator.generateSlots(today); // [18:00:00,19:00:00,20:00:00]

        assertThat(result).size().isEqualTo(3);
    }

    @Test
    void get_slots_when_is_today_at_time_5_0(){

        LocalDateTime instant = LocalDateTime.of(2025, Month.JUNE, 2, 5, 0);
        ZoneId zona = ZoneId.systemDefault();
        fixedClock = Clock.fixed( instant.atZone(zona).toInstant(), zona );

        validator = new SlotGenerationValidator(
                6,
                17,
                6,
                20,
                3,
                60,
                fixedClock
        );

        generator = new GenerateSessionSlots(sessionConfigMock, validator, fixedClock);

        LocalDate today = LocalDate.of(2025,Month.JUNE,2);

        List<LocalTime> slots = generator.generateSlots(today);

        assertThat(slots.size()).isEqualTo(0);
    }


    @Test
    void get_slots_for_a_future_date(){
        LocalDateTime instant = LocalDateTime.of(2025, Month.JUNE, 2, 14, 15);
        ZoneId zona = ZoneId.systemDefault();
        fixedClock = Clock.fixed( instant.atZone(zona).toInstant(), zona );

        validator = new SlotGenerationValidator(
                6,
                17,
                6,
                20,
                3,
                60,
                fixedClock
        );

        generator = new GenerateSessionSlots(sessionConfigMock, validator, fixedClock);

        LocalDate futureDate =  LocalDate.of(2025,Month.JUNE,5);

        List<LocalTime> result = generator.generateSlots(futureDate);

        assertThat(result).size().isEqualTo(13);
    }

}