package com.devprep.devprepai.service;

import com.devprep.devprepai.dto.CategoryRequest;
import com.devprep.devprepai.dto.CategoryResponse;
import com.devprep.devprepai.entity.Category;
import com.devprep.devprepai.entity.User;
import com.devprep.devprepai.exception.InvalidCategoryException;
import com.devprep.devprepai.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AuthService authService;

    public CategoryService(
            CategoryRepository categoryRepository, AuthService authService) {
        this.categoryRepository = categoryRepository;
        this.authService = authService;
    }

    @Transactional
    public CategoryResponse addCategory(CategoryRequest request){
        User user = authService.currentUser();
        if(categoryRepository.existsByNameAndUser(request.name(), user)){
            throw new InvalidCategoryException("Category already exists");
        }

        Category category = new Category(
                request.name(),
                user
        );
        Category savedCategory = categoryRepository.save(category);
        return addCategoryResponse(savedCategory);
    }

    public CategoryResponse editCategory(CategoryRequest request){
        User user = authService.currentUser();
        if(!categoryRepository.existsByNameAndUser(request.name(), user)){
            throw new InvalidCategoryException("Category does not exists");
        }

        Category category = new Category(
                request.name(),
                user
        );
        Category savedCategory = categoryRepository.save(category);
        return addCategoryResponse(savedCategory);
    }

    private CategoryResponse addCategoryResponse(Category category){
        return new CategoryResponse(category.getUser().getId(), category.getName());
    }
}
