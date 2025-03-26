package ru.afishaBMSTU.controller.compilation;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.afishaBMSTU.dto.compilation.*;
import ru.afishaBMSTU.service.compilation.admin.CompilationAdminService;

@RestController
@RequestMapping("/api/admin/compilations")
@RequiredArgsConstructor
@Tag(name = "Админ: Подборки", description = "API для управления подборками мероприятий")
@SecurityRequirement(name = "bearerAuth")
public class CompilationAdminController {

    private final CompilationAdminService compilationAdminService;

    @Operation(summary = "Создать подборку", description = "Создание новой подборки мероприятий")
    @ApiResponse(responseCode = "201", description = "Подборка создана",
            content = @Content(schema = @Schema(implementation = CompilationDto.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(
            @RequestBody @Valid NewCompilationDto newCompilationDto) {
        return compilationAdminService.createCompilation(newCompilationDto);
    }

    @Operation(summary = "Обновить подборку", description = "Обновление существующей подборки")
    @ApiResponse(responseCode = "200", description = "Подборка обновлена",
            content = @Content(schema = @Schema(implementation = CompilationDto.class)))
    @PatchMapping("{compId}")
    public CompilationDto updateCompilation(
            @Parameter(description = "ID подборки", required = true, example = "1")
            @PathVariable Long compId,
            @RequestBody @Valid UpdateCompilationRequest newCompilationDto) {
        return compilationAdminService.updateCompilation(newCompilationDto, compId);
    }

    @Operation(summary = "Удалить подборку", description = "Удаление подборки по ID")
    @ApiResponse(responseCode = "204", description = "Подборка удалена")
    @DeleteMapping("{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(
            @Parameter(description = "ID подборки", required = true, example = "1")
            @PathVariable Long compId) {
        compilationAdminService.deleteCompilation(compId);
    }
}