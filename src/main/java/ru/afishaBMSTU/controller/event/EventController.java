package ru.afishaBMSTU.controller.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.afishaBMSTU.dto.event.EventFullDto;
import ru.afishaBMSTU.dto.event.EventShortDto;
import ru.afishaBMSTU.dto.event.NewEventDto;
import ru.afishaBMSTU.dto.event.UpdateEventUserRequest;
import ru.afishaBMSTU.dto.request.EventRequestStatusUpdateRequest;
import ru.afishaBMSTU.dto.request.EventRequestStatusUpdateResult;
import ru.afishaBMSTU.dto.request.ParticipationRequestDto;
import ru.afishaBMSTU.model.user.CustomUserDetails;
import ru.afishaBMSTU.service.event.EventService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @RequestBody @Valid NewEventDto newEventDto) {
        return eventService.createEvent(newEventDto, userDetails.getId());
    }

    @RequestMapping(value = "/image/{eventId}", method = {RequestMethod.POST, RequestMethod.PATCH},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadImageToEvent(@RequestParam("image") MultipartFile image, @PathVariable Long eventId,
                                     @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        return eventService.uploadImage(image, userDetails.getId(), eventId);
    }

    @GetMapping
    public List<EventShortDto> getEventsByUserId(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getAllEventsByUserId(userDetails.getId(), from, size);
    }

    @GetMapping("{eventId}")
    public EventFullDto getEventByUserIdAndEventId(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long eventId) {
        return eventService.getEventByUserIdAndEventId(userDetails.getId(), eventId);
    }

    @PatchMapping("{eventId}")
    public EventFullDto updateEvent(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return eventService.updateEvent(eventId, updateEventUserRequest, userDetails.getId());
    }

    @GetMapping("{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByEventId(@PathVariable Long eventId) {
        return eventService.getRequests(eventId);
    }

    @PatchMapping("{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequest(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @PathVariable Long eventId,
                                                        @RequestBody EventRequestStatusUpdateRequest request) {
        return eventService.updateRequestStatus(userDetails.getId(), eventId, request);
    }
}
