package ru.afishaBMSTU.service.event;

import ru.afishaBMSTU.dto.comment.CommentDto;
import ru.afishaBMSTU.dto.event.EventFilterDto;
import ru.afishaBMSTU.dto.event.EventFullDto;

import java.util.List;

public interface PublicEventService {
    List<EventFullDto> getEvents(EventFilterDto filters, String sort, int from, int size);
    EventFullDto getEvent(Long id, String ip);
    List<CommentDto> getCommentsByEvent(Long eventId, Integer from, Integer size);
}
