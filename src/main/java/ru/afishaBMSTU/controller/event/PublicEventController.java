package ru.afishaBMSTU.controller.event;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.afishaBMSTU.dto.comment.CommentDto;
import ru.afishaBMSTU.dto.event.*;
import ru.afishaBMSTU.service.event.PublicEventService;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
@Tag(name = "Публичные мероприятия", description = "Публичный API для работы с мероприятиями")
@SecurityRequirement(name = "bearerAuth")
public class PublicEventController {

    private final PublicEventService publicEventService;

    @Operation(summary = "Получить мероприятия",
            description = "Получение списка мероприятий с фильтрацией, сортировкой и пагинацией")
    @GetMapping
    public List<EventFullDto> getEvents(
            @ModelAttribute EventFilterDto eventFilterDto,
            @Parameter(description = "Сортировка (asc/desc)", example = "asc")
            @RequestParam(required = false) String sort,
            @Parameter(description = "Начальная позиция", example = "0")
            @RequestParam(defaultValue = "0") Integer from,
            @Parameter(description = "Количество элементов", example = "10")
            @RequestParam(defaultValue = "10") Integer size) {
        return publicEventService.getEvents(eventFilterDto, sort, from, size);
    }

    @Operation(summary = "Получить мероприятие по ID",
            description = "Получение полной информации о мероприятии с учетом IP для статистики")
    @GetMapping("{id}")
    public EventFullDto getEvent(
            @Parameter(description = "ID мероприятия", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(hidden = true) HttpServletRequest request) {
        return publicEventService.getEvent(id, request.getRemoteAddr());
    }

    @Operation(summary = "Получить комментарии к мероприятию",
            description = "Получение списка комментариев для конкретного мероприятия")
    @GetMapping("{eventId}/comments")
    public List<CommentDto> getCommentByEvent(
            @Parameter(description = "ID мероприятия", required = true, example = "1")
            @PathVariable Long eventId,
            @Parameter(description = "Начальная позиция", example = "0")
            @RequestParam(defaultValue = "0") Integer from,
            @Parameter(description = "Количество элементов", example = "10")
            @RequestParam(defaultValue = "10") Integer size) {
        return publicEventService.getCommentsByEvent(eventId, from, size);
    }
}