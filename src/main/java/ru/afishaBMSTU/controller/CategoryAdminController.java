package ru.afishaBMSTU.controller;

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
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryAdminService categoryAdminService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        return categoryAdminService.createCategory(categoryDto);
    }

    @PatchMapping("{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId, @Valid @RequestBody NewCategoryDto categoryDto) {
        return categoryAdminService.updateCategory(categoryDto, catId);
    }

    @DeleteMapping("{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        categoryAdminService.deleteCategory(catId);
    }
}
