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
    private SlotGenerationValidator validatorMock;
    private Clock fixedClock;
    private GenerateSessionSlots generator;

    @BeforeEach
    void setUp() {
        sessionConfigMock = Mockito.mock(SessionConfig.class);

        when(sessionConfigMock.getDuration()).thenReturn(60);
        when(sessionConfigMock.getEarliestStartWorkingHour()).thenReturn(8);
        when(sessionConfigMock.getLatestStartWorkingHour()).thenReturn(20);
        when(sessionConfigMock.getAdvanceHours()).thenReturn(3);

        LocalDateTime instant = LocalDateTime.of(2025, Month.JUNE, 2, 14, 15);
        ZoneId zona = ZoneId.systemDefault();
        fixedClock = Clock.fixed( instant.atZone(zona).toInstant(), zona );

        validatorMock = new SlotGenerationValidator(
                6,
                17,
                6,
                20,
                3,
                60,
                fixedClock
        );

        generator = new GenerateSessionSlots(sessionConfigMock, validatorMock, fixedClock);
    }

    @Test
    void generateSlotsForToday() {

        LocalDate today = LocalDate.of(2025, Month.JUNE, 2);

        List<LocalTime> result = generator.generateSlots(today);

        System.out.println(result);

        assertThat(result).size().isEqualTo(3);


//        verifyNoInteractions(validatorMock);
    }

}