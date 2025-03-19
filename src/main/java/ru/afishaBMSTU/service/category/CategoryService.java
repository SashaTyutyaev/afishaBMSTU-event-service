package ru.afishaBMSTU.service.category;

import ru.afishaBMSTU.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories(Integer from, Integer size);
    CategoryDto getCategory(Long id);
}
