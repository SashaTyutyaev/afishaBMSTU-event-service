package ru.afishaBMSTU.controller.request;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.afishaBMSTU.dto.jwt.JwtTokenDataDto;
import ru.afishaBMSTU.dto.request.ParticipationRequestDto;
import ru.afishaBMSTU.service.request.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/requests")
@Tag(name = "Заявки пользователей", description = "API для работы с заявками на участие")
@SecurityRequirement(name = "bearerAuth")
public class RequestController {

    private final RequestService requestService;

    @Operation(summary = "Создать заявку",
            description = "Создание заявки на участие в мероприятии. Требуется роль USER, CREATOR или ADMIN")
    @ApiResponse(responseCode = "201", description = "Заявка создана",
            content = @Content(schema = @Schema(implementation = ParticipationRequestDto.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(
            @Parameter(hidden = true) @AuthenticationPrincipal JwtTokenDataDto jwtTokenDataDto,
            @Parameter(description = "ID мероприятия", required = true, example = "1")
            @RequestParam Long eventId) {
        return requestService.createRequest(jwtTokenDataDto.getExternalId(), eventId);
    }

    @Operation(summary = "Получить мои заявки",
            description = "Получение списка всех заявок текущего пользователя")
    @GetMapping
    public List<ParticipationRequestDto> getRequests(
            @Parameter(hidden = true) @AuthenticationPrincipal JwtTokenDataDto jwtTokenDataDto) {
        return requestService.getRequests(jwtTokenDataDto.getExternalId());
    }

    @Operation(summary = "Отменить заявку",
            description = "Отмена своей заявки на участие в мероприятии")
    @ApiResponse(responseCode = "200", description = "Заявка отменена",
            content = @Content(schema = @Schema(implementation = ParticipationRequestDto.class)))
    @PatchMapping("{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(
            @Parameter(hidden = true) @AuthenticationPrincipal JwtTokenDataDto jwtTokenDataDto,
            @Parameter(description = "ID заявки", required = true, example = "1")
            @PathVariable Long requestId) {
        return requestService.cancelRequest(jwtTokenDataDto.getExternalId(), requestId);
    }
}