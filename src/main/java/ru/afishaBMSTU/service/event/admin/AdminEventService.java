package ru.afishaBMSTU.service.event.admin;

import ru.afishaBMSTU.dto.event.EventAdminFilterDto;
import ru.afishaBMSTU.dto.event.EventFullDto;
import ru.afishaBMSTU.dto.event.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventService {
    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEvent);
    List<EventFullDto> getEvents(EventAdminFilterDto filterDto, Integer from, Integer size);
}
