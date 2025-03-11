package ru.afishaBMSTU.users.events;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.afishaBMSTU.users.events.model.dto.EventFullDto;
import ru.afishaBMSTU.users.events.model.dto.EventRequestStatusUpdateRequest;
import ru.afishaBMSTU.users.events.model.dto.EventRequestStatusUpdateResult;
import ru.afishaBMSTU.users.events.model.dto.EventShortDto;
import ru.afishaBMSTU.users.events.model.dto.NewEventDto;
import ru.afishaBMSTU.users.events.model.dto.UpdateEventUserRequest;
import ru.afishaBMSTU.users.requests.model.dto.ParticipationRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable("userId") Long userId, @Valid @RequestBody NewEventDto event) {
        return eventService.createEvent(event, userId);
    }

    @GetMapping
    public List<EventShortDto> getEventsByUserId(@PathVariable Long userId,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getAllEventsByUserId(userId, from, size);
    }

    @GetMapping("{eventId}")
    public EventFullDto getEventByUserIdAndEventId(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getEventByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return eventService.updateEvent(eventId, updateEventUserRequest, userId);
    }

    @GetMapping("{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByEventId(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping("{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequest(@PathVariable Long userId, @PathVariable Long eventId,
                                                        @RequestBody EventRequestStatusUpdateRequest request) {
        return eventService.updateRequestStatus(userId, eventId, request);
    }
}
