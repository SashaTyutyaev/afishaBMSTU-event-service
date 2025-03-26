package ru.afishaBMSTU.service.request;

import ru.afishaBMSTU.dto.request.ParticipationRequestDto;

import java.util.List;
import java.util.UUID;

public interface RequestService {
    ParticipationRequestDto createRequest(UUID externalId, Long eventId);
    ParticipationRequestDto cancelRequest(UUID externalId, Long requestId);
    List<ParticipationRequestDto> getRequests(UUID externalId);
}
