package ru.afishaBMSTU.admin.categories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.afishaBMSTU.admin.categories.model.Category;
import ru.afishaBMSTU.admin.categories.model.dto.CategoryDto;
import ru.afishaBMSTU.admin.categories.model.dto.CategoryMapper;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.users.events.EventRepository;
import ru.afishaBMSTU.users.events.model.Event;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryDto addCategory(CategoryDto categoryDto) {
        try {
            Category category = CategoryMapper.toCategory(categoryDto);
            categoryRepository.save(category);
            log.info("Successfully added category: {}", category);
            return CategoryMapper.toCategoryDto(category);
        } catch (DataIntegrityViolationException e) {
            log.error("Name already in use: {}", categoryDto.getName());
            throw new DataIntegrityViolationException("Name already in use");
        }
    }

    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId) {
        try {
            Category category = getCategoryById(catId);
            category.setName(categoryDto.getName());
            categoryRepository.save(category);
            log.info("Successfully updated category: {}", category);
            return CategoryMapper.toCategoryDto(category);
        } catch (DataIntegrityViolationException e) {
            log.error("Name already in use: {}", categoryDto.getName());
            throw new DataIntegrityViolationException("Name already in use");
        }
    }

    public void deleteCategory(Long catId) {
        getCategoryById(catId);

        List<Event> events = eventRepository.findAllByCategoryId(catId);

        if (events.isEmpty()) {
            categoryRepository.deleteById(catId);
            log.info("Successfully deleted category: {}", catId);
        } else {
            log.error("There are event in this category: {}", catId);
            throw new DataIntegrityViolationException("There are event in this category");
        }
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> {
            log.error("Category with id {} not found", id);
            return new NotFoundException("Category with id " + id + " not found");
        });
    }
}
