package com.dev.phosell.session.application.service;

import com.dev.phosell.session.domain.model.SessionStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class GetAvailableSessionSlotsService {
    private final HandlePhotographersWithSessionSlotsService handlePhotographersWithSessionSlotsService;

    public GetAvailableSessionSlotsService(
            HandlePhotographersWithSessionSlotsService handlePhotographersWithSessionSlotsService
    ){
        this.handlePhotographersWithSessionSlotsService = handlePhotographersWithSessionSlotsService;
    }

    public List<LocalTime> getAvailableSlots(LocalDate date)
    {

        // List of free statuses
        List<String> freeStatuses = List.of(
                SessionStatus.COMPLETED.toString(),
                SessionStatus.CANCELLED_BY_ADMIN.toString(),
                SessionStatus.CANCELLED_BY_CLIENT.toString()
        );

        handlePhotographersWithSessionSlotsService.loadAllSlots(date);
        handlePhotographersWithSessionSlotsService.loadBusySessions(date,freeStatuses);
        handlePhotographersWithSessionSlotsService.loadPhotographersInService();
        handlePhotographersWithSessionSlotsService.calculateAllAvailableSlots();
        List<LocalTime> availableSlots = handlePhotographersWithSessionSlotsService.getAvailableSlots();

        return  availableSlots;

    }
}
