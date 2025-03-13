package ru.afishaBMSTU.events;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.afishaBMSTU.events.dto.EventFilterDto;
import ru.afishaBMSTU.users.comments.model.CommentDto;
import ru.afishaBMSTU.users.events.model.dto.EventFullDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {

    private final PublicEventService publicEventService;

    @GetMapping
    public List<EventFullDto> getEvents(@ModelAttribute EventFilterDto eventFilterDto,
                                        @RequestParam(required = false) String sort,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return publicEventService.getEvents(eventFilterDto, sort, from, size);
    }

    @GetMapping("{id}")
    public EventFullDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        return publicEventService.getEvent(id, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("{eventId}/comments")
    public List<CommentDto> getCommentByEvent(@PathVariable Long eventId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return publicEventService.getCommentsByEvent(eventId, from, size);
    }
}
