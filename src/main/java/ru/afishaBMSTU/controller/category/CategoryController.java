package ru.afishaBMSTU.controller.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.afishaBMSTU.dto.category.CategoryDto;
import ru.afishaBMSTU.service.category.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Категории", description = "API для работы с категориями мероприятий")
public class CategoryController {

    private final CategoryService categoryServiceImpl;

    @Operation(summary = "Получить список категорий", description = "Возвращает список категорий с пагинацией")
    @GetMapping
    public List<CategoryDto> getCategories(
            @Parameter(description = "Начальная позиция", example = "0") @RequestParam(defaultValue = "0") Integer from,
            @Parameter(description = "Количество элементов", example = "10") @RequestParam(defaultValue = "10") Integer size) {
        return categoryServiceImpl.getCategories(from, size);
    }

    @Operation(summary = "Получить категорию по ID", description = "Возвращает данные конкретной категории")
    @GetMapping("{catId}")
    public CategoryDto getCategory(
            @Parameter(description = "ID категории", required = true, example = "1") @PathVariable Long catId) {
        return categoryServiceImpl.getCategory(catId);
    }
}