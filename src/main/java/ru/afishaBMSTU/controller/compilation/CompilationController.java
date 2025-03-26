package ru.afishaBMSTU.controller.compilation;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.afishaBMSTU.dto.compilation.CompilationDto;
import ru.afishaBMSTU.service.compilation.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/compilations")
@Tag(name = "Подборки", description = "API для работы с подборками мероприятий")
@SecurityRequirement(name = "bearerAuth")
public class CompilationController {

    private final CompilationService compilationService;

    @Operation(summary = "Получить подборки",
            description = "Возвращает список подборок с возможностью фильтрации по закреплению")
    @GetMapping
    public List<CompilationDto> getCompilations(
            @Parameter(description = "Фильтр по закрепленным подборкам", example = "true")
            @RequestParam(required = false) Boolean pinned,
            @Parameter(description = "Начальная позиция", example = "0")
            @RequestParam(defaultValue = "0") Integer from,
            @Parameter(description = "Количество элементов", example = "10")
            @RequestParam(defaultValue = "10") Integer size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @Operation(summary = "Получить подборку по ID",
            description = "Возвращает полную информацию о конкретной подборке")
    @GetMapping("{compId}")
    public CompilationDto getCompilation(
            @Parameter(description = "ID подборки", required = true, example = "1")
            @PathVariable Long compId) {
        return compilationService.getCompilation(compId);
    }
}