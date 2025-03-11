package ru.afishaBMSTU.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.afishaBMSTU.admin.categories.CategoryRepository;
import ru.afishaBMSTU.admin.categories.model.Category;
import ru.afishaBMSTU.admin.categories.model.dto.CategoryDto;
import ru.afishaBMSTU.admin.categories.model.dto.CategoryMapper;
import ru.afishaBMSTU.exceptions.IncorrectParameterException;
import ru.afishaBMSTU.exceptions.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pageable = validatePageable(from, size);
        List<Category> categories = categoryRepository.findAll(pageable).getContent();
        log.info("Get all categories successful");
        return categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    public CategoryDto getCategory(Long id) {
        Category category = getCategoryById(id);
        log.info("Get category {} successful", id);
        return CategoryMapper.toCategoryDto(category);
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> {
            log.error("Category with id {} not found", id);
            return new NotFoundException("Category with id " + id + " not found");
        });
    }

    private PageRequest validatePageable(Integer from, Integer size) {
        if (from == null || from < 0) {
            log.error("Params must be greater than 0");
            throw new IncorrectParameterException("Params must be greater than 0");
        }
        if (size == null || size < 0) {
            log.error("Params must be greater than 0");
            throw new IncorrectParameterException("Params must be greater than 0");
        }

        return PageRequest.of(from / size, size);
    }
}
