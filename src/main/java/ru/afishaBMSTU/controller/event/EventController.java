package ru.afishaBMSTU.controller.event;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.afishaBMSTU.dto.jwt.JwtTokenDataDto;
import ru.afishaBMSTU.dto.request.EventRequestStatusUpdateRequest;
import ru.afishaBMSTU.dto.request.EventRequestStatusUpdateResult;
import ru.afishaBMSTU.dto.request.ParticipationRequestDto;
import ru.afishaBMSTU.service.event.EventService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/creator/events")
@RequiredArgsConstructor
@Tag(name = "Creator: Мероприятия", description = "API для организаторов мероприятий")
@SecurityRequirement(name = "bearerAuth")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "Создать мероприятие", description = "Требуется роль CREATOR или ADMIN")
    @ApiResponse(responseCode = "201", description = "Мероприятие создано")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(
            @Parameter(hidden = true) @AuthenticationPrincipal JwtTokenDataDto jwtTokenDataDto,
            @RequestBody @Valid NewEventDto newEventDto) {
        return eventService.createEvent(newEventDto, jwtTokenDataDto.getExternalId());
    }

    @Operation(summary = "Загрузить изображение", description = "Загрузка изображения для мероприятия")
    @RequestMapping(value = "/image/{eventId}", method = {RequestMethod.POST, RequestMethod.PATCH},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadImageToEvent(
            @Parameter(description = "Файл изображения", required = true) @RequestParam("image") MultipartFile image,
            @Parameter(description = "ID мероприятия", example = "1") @PathVariable Long eventId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtTokenDataDto jwtTokenDataDto) throws IOException {
        return eventService.uploadImage(image, jwtTokenDataDto.getExternalId(), eventId);
    }

    @Operation(summary = "Получить мои мероприятия", description = "Список мероприятий текущего организатора")
    @GetMapping
    public List<EventShortDto> getEventsByUserId(
            @Parameter(hidden = true) @AuthenticationPrincipal JwtTokenDataDto jwtTokenDataDto,
            @Parameter(description = "Смещение", example = "0") @RequestParam(defaultValue = "0") Integer from,
            @Parameter(description = "Лимит", example = "10") @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getAllEventsByUserId(jwtTokenDataDto.getExternalId(), from, size);
    }

    @Operation(summary = "Получить мероприятие", description = "Полная информация о конкретном мероприятии организатора")
    @GetMapping("{eventId}")
    public EventFullDto getEventByUserIdAndEventId(
            @Parameter(hidden = true) @AuthenticationPrincipal JwtTokenDataDto jwtTokenDataDto,
            @Parameter(description = "ID мероприятия", example = "1") @PathVariable Long eventId) {
        return eventService.getEventByUserIdAndEventId(jwtTokenDataDto.getExternalId(), eventId);
    }

    @Operation(summary = "Обновить мероприятие", description = "Редактирование данных мероприятия")
    @PatchMapping("{eventId}")
    public EventFullDto updateEvent(
            @Parameter(hidden = true) @AuthenticationPrincipal JwtTokenDataDto jwtTokenDataDto,
            @Parameter(description = "ID мероприятия", example = "1") @PathVariable Long eventId,
            @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return eventService.updateEvent(eventId, updateEventUserRequest, jwtTokenDataDto.getExternalId());
    }

    @Operation(summary = "Получить заявки", description = "Заявки на участие в конкретном мероприятии")
    @GetMapping("{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByEventId(
            @Parameter(description = "ID мероприятия", example = "1") @PathVariable Long eventId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtTokenDataDto jwtTokenDataDto) {
        return eventService.getRequests(eventId, jwtTokenDataDto.getExternalId());
    }

    @Operation(summary = "Обновить статус заявок", description = "Подтверждение/отклонение заявок на участие")
    @PatchMapping("{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequest(
            @Parameter(hidden = true) @AuthenticationPrincipal JwtTokenDataDto jwtTokenDataDto,
            @Parameter(description = "ID мероприятия", example = "1") @PathVariable Long eventId,
            @RequestBody EventRequestStatusUpdateRequest request) {
        return eventService.updateRequestStatus(jwtTokenDataDto.getExternalId(), eventId, request);
    }
}
