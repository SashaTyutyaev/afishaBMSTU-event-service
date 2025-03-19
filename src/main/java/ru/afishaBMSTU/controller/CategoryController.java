package ru.afishaBMSTU.controller;

import lombok.RequiredArgsConstructor;
import ru.afishaBMSTU.dto.category.CategoryDto;
import org.springframework.web.bind.annotation.*;
import ru.afishaBMSTU.service.category.CategoryService;
import ru.afishaBMSTU.service.category.CategoryServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryServiceImpl;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return categoryServiceImpl.getCategories(from, size);
    }

    @GetMapping("{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        return categoryServiceImpl.getCategory(catId);
    }
}
