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
import java.util.UUID;

public interface EventService {
    EventFullDto createEvent(NewEventDto newEventDto, UUID externalId);

    List<EventShortDto> getAllEventsByUserId(UUID externalId, Integer from, Integer size);

    EventFullDto getEventByUserIdAndEventId(UUID externalId, Long eventId);

    EventFullDto updateEvent(Long eventId, UpdateEventUserRequest updatedEvent, UUID externalId);

    List<ParticipationRequestDto> getRequests(Long eventId, UUID externalId);

    EventRequestStatusUpdateResult updateRequestStatus(UUID externalId, Long eventId,
                                                       EventRequestStatusUpdateRequest updateRequest);

    String uploadImage(MultipartFile file, UUID externalId, Long eventId) throws IOException;
}
