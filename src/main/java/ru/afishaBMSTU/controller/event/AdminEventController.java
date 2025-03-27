package ru.afishaBMSTU.controller.event;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.afishaBMSTU.dto.event.*;
import ru.afishaBMSTU.service.event.admin.AdminEventService;
import java.util.List;

@RestController
@RequestMapping("/api/admin/events")
@RequiredArgsConstructor
@Tag(name = "Админ: Мероприятия", description = "API для администрирования мероприятий")
@SecurityRequirement(name = "bearerAuth")
public class AdminEventController {

    private final AdminEventService adminEventService;

    @Operation(summary = "Получить мероприятия (админ)",
            description = "Фильтрация мероприятий с пагинацией")
    @GetMapping
    public List<EventFullDto> getEvents(
            @ModelAttribute EventAdminFilterDto filterDto,
            @Parameter(description = "Смещение", example = "0") @RequestParam(defaultValue = "0") Integer from,
            @Parameter(description = "Лимит", example = "10") @RequestParam(defaultValue = "10") Integer size) {
        return adminEventService.getEvents(filterDto, from, size);
    }

    @Operation(summary = "Обновить мероприятие",
            description = "Обновление данных мероприятия администратором")
    @ApiResponse(responseCode = "200", description = "Мероприятие обновлено",
            content = @Content(schema = @Schema(implementation = EventFullDto.class)))
    @PatchMapping("{eventId}")
    public EventFullDto updateEvent(
            @Parameter(description = "ID мероприятия", required = true, example = "1")
            @PathVariable Long eventId,
            @RequestBody @Valid UpdateEventAdminRequest event) {
        return adminEventService.updateEvent(eventId, event);
    }
}