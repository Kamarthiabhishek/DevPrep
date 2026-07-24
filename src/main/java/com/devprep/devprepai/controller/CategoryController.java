package com.devprep.devprepai.controller;

import com.devprep.devprepai.dto.CategoryRequest;
import com.devprep.devprepai.dto.CategoryResponse;
import com.devprep.devprepai.service.CategoryService;
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
    public ResponseEntity<CategoryResponse> CreateCategory(@RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(request));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories());
    }

    @PatchMapping("/categories/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody CategoryRequest request, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.editCategory(request, id));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.deleteCategory(id));
    }
}
