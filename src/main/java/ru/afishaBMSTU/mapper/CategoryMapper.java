package ru.afishaBMSTU.mapper;

import ru.afishaBMSTU.dto.category.CategoryDto;
import ru.afishaBMSTU.dto.category.NewCategoryDto;
import ru.afishaBMSTU.model.category.Category;

public class CategoryMapper {

    public static Category toCategory(NewCategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
