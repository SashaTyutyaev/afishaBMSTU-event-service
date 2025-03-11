package ru.afishaBMSTU.events;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ru.afishaBMSTU.users.comments.model.CommentDto;
import ru.afishaBMSTU.users.events.model.dto.EventFullDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {

    private final PublicEventService publicEventService;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) String text,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false, defaultValue = "false") Boolean paid,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                        @RequestParam(required = false) String sort,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        HttpServletRequest request) {
        return publicEventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size,
                request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("{id}")
    public EventFullDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        return publicEventService.getEvent(id, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("{eventId}/comments")
    public List<CommentDto> getCommentByEvent(@PathVariable Long eventId, @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return publicEventService.getCommentsByEvent(eventId, from, size);
    }
}
