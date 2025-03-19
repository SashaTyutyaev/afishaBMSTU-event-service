package ru.afishaBMSTU.service.request;

import ru.afishaBMSTU.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto createRequest(Long userId, Long eventId);
    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
    List<ParticipationRequestDto> getRequests(Long userId);
}
