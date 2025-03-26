package ru.afishaBMSTU.controller.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.afishaBMSTU.dto.category.CategoryDto;
import ru.afishaBMSTU.dto.category.NewCategoryDto;
import ru.afishaBMSTU.service.category.admin.CategoryAdminService;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Tag(name = "Администрирование категорий", description = "API для управления категориями мероприятий")
@SecurityRequirement(name = "bearerAuth")
public class CategoryAdminController {

    private final CategoryAdminService categoryAdminService;

    @Operation(summary = "Создание новой категории",
            description = "Создает новую категорию мероприятий. Требуются права ADMIN",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания категории",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NewCategoryDto.class)))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Категория успешно создана",
                    content = @Content(schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401",
                    description = "Требуется авторизация"),
            @ApiResponse(responseCode = "403",
                    description = "Недостаточно прав (требуется роль ADMIN)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        return categoryAdminService.createCategory(categoryDto);
    }

    @Operation(summary = "Обновление категории", description = "Обновляет данные существующей категории. Требуются права ADMIN",
            parameters = {@Parameter(name = "catId", description = "ID категории для обновления", required = true, example = "1")},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Новые данные категории",
                    required = true, content = @Content(schema = @Schema(implementation = NewCategoryDto.class)))
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория успешно обновлена",
                    content = @Content(schema = @Schema(implementation = CategoryDto.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав (требуется роль ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @PatchMapping("{catId}")
    public CategoryDto updateCategory(
            @PathVariable Long catId,
            @Valid @RequestBody NewCategoryDto categoryDto) {
        return categoryAdminService.updateCategory(categoryDto, catId);
    }

    @Operation(summary = "Удаление категории", description = "Удаляет категорию по идентификатору. Требуются права ADMIN",
            parameters = {@Parameter(name = "catId", description = "ID категории для удаления", required = true, example = "1")}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Категория успешно удалена"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав (требуется роль ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @DeleteMapping("{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        categoryAdminService.deleteCategory(catId);
    }
}