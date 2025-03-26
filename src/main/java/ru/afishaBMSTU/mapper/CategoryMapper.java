package ru.afishaBMSTU.mapper;

import org.mapstruct.Mapper;
import ru.afishaBMSTU.dto.category.CategoryDto;
import ru.afishaBMSTU.dto.category.NewCategoryDto;
import ru.afishaBMSTU.model.category.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(NewCategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);
}
