package ru.afishaBMSTU.controller.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.afishaBMSTU.dto.comment.CommentDto;
import ru.afishaBMSTU.dto.event.EventFilterDto;
import ru.afishaBMSTU.dto.event.EventFullDto;
import ru.afishaBMSTU.service.event.PublicEventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
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
        return publicEventService.getEvent(id, request.getRemoteAddr());
    }

    @GetMapping("{eventId}/comments")
    public List<CommentDto> getCommentByEvent(@PathVariable Long eventId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return publicEventService.getCommentsByEvent(eventId, from, size);
    }
}
