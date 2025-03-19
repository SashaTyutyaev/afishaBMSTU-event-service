package ru.afishaBMSTU.service.category.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.afishaBMSTU.dto.category.CategoryDto;
import ru.afishaBMSTU.dto.category.NewCategoryDto;
import ru.afishaBMSTU.exceptions.NotFoundException;
import ru.afishaBMSTU.mapper.CategoryMapper;
import ru.afishaBMSTU.model.category.Category;
import ru.afishaBMSTU.model.event.Event;
import ru.afishaBMSTU.repository.CategoryRepository;
import ru.afishaBMSTU.repository.EventRepository;

import java.util.List;

@Service("adminCategoryService")
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminServiceImpl implements CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
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

    @Override
    @Transactional
    public CategoryDto updateCategory(NewCategoryDto categoryDto, Long catId) {
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

    @Override
    @Transactional
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
