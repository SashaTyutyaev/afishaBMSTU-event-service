package ru.afishaBMSTU.service.category.admin;

import ru.afishaBMSTU.dto.category.CategoryDto;
import ru.afishaBMSTU.dto.category.NewCategoryDto;

public interface CategoryAdminService {
    CategoryDto createCategory(NewCategoryDto newCategoryDto);
    CategoryDto updateCategory(NewCategoryDto categoryDto, Long id);
    void deleteCategory(Long id);
}
