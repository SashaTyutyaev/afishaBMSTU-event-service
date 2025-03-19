package ru.afishaBMSTU.service.event;

import org.springframework.web.multipart.MultipartFile;
import ru.afishaBMSTU.dto.event.EventFullDto;
import ru.afishaBMSTU.dto.event.EventShortDto;
import ru.afishaBMSTU.dto.event.NewEventDto;
import ru.afishaBMSTU.dto.event.UpdateEventUserRequest;
import ru.afishaBMSTU.dto.request.EventRequestStatusUpdateRequest;
import ru.afishaBMSTU.dto.request.EventRequestStatusUpdateResult;
import ru.afishaBMSTU.dto.request.ParticipationRequestDto;

import java.io.IOException;
import java.util.List;

public interface EventService {
    EventFullDto createEvent(NewEventDto newEventDto, Long userId);
    List<EventShortDto> getAllEventsByUserId(Long userId, Integer from, Integer size);
    EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId);
    EventFullDto updateEvent(Long eventId, UpdateEventUserRequest updatedEvent, Long userId);
    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);
    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId,
                                                       EventRequestStatusUpdateRequest updateRequest);
    String uploadImage(MultipartFile file, Long userId, Long eventId) throws IOException;
}
