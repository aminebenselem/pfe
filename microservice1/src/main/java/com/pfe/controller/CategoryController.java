package com.pfe.controller;

import com.pfe.entity.Category;
import com.pfe.repository.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return (List<Category>) categoryRepository.findAll();
    }
}
