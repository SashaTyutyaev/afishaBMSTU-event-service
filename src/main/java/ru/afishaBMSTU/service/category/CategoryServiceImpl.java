package ru.afishaBMSTU.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.afishaBMSTU.dto.category.CategoryDto;
import ru.afishaBMSTU.exceptions.IncorrectParameterException;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.mapper.CategoryMapper;
import ru.afishaBMSTU.model.category.Category;
import ru.afishaBMSTU.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable pageable = validatePageable(from, size);
        List<Category> categories = categoryRepository.findAll(pageable).getContent();
        log.info("Get all categories successful");
        return categories.stream().map(categoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long id) {
        Category category = getCategoryById(id);
        log.info("Get category {} successful", id);
        return categoryMapper.toCategoryDto(category);
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
