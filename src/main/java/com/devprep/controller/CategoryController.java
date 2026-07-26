package com.devprep.controller;

import com.devprep.dto.CategoryRequest;
import com.devprep.dto.CategoryResponse;
import com.devprep.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(request));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories());
    }

    @PatchMapping("/categories/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@Valid @RequestBody CategoryRequest request, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.editCategory(request, id));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(categoryService.deleteCategory(id));
    }
}
