package com.devprep.devprepai.service;

import com.devprep.devprepai.dto.CategoryRequest;
import com.devprep.devprepai.dto.CategoryResponse;
import com.devprep.devprepai.entity.Category;
import com.devprep.devprepai.entity.User;
import com.devprep.devprepai.exception.InvalidCategoryException;
import com.devprep.devprepai.repository.CategoryRepository;
import com.devprep.devprepai.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
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
        log.info("Add category request for : {} by user {}", request.name(), user.getName());

        if(categoryRepository.existsByNameAndUser(request.name(), user)){
            log.warn("Category with name {} already exists for user {}", request.name(), user.getName());
            throw new InvalidCategoryException("Category with name " + request.name() + " already exists for user " + user.getName());
        }
        Category req = new Category(
                request.name(),
                user
        );
        Category savedCategory = categoryRepository.save(req);
        log.info("Category saved successfully : categoryId : {} , Name {}", savedCategory.getCategoryId(), savedCategory.getName());
        return addCategoryResponse(savedCategory);
    }

    @Transactional
    public CategoryResponse editCategory(CategoryRequest request, Long id){
        User user = authService.currentUser();

        log.info("Edit category request from : {}", user.getEmail());
        Category req = categoryRepository.findByCategoryIdAndUser(id, user).orElseThrow(
                () -> {
                    log.warn("Category with id {} doesn't exists for user {}", id, user.getName());
                    return new InvalidCategoryException("Category with id " + id + " doesn't exists for user " + user.getName());
                }
        );

        if(!req.getName().equals(request.name()) &&
                categoryRepository.existsByNameAndUser(request.name(), user)) {
            log.warn("Category {} already exists for user {}", request.name(), user.getName());
            throw new InvalidCategoryException("Category with name " + request.name() + " doesn't exists for user " + user.getName());
        }

        log.info("Category edit request received for :{}", req.getName());
        req.setName(request.name());
        Category savedCategory = categoryRepository.save(req);
        log.info("Category updated for : id {} with name {}", savedCategory.getCategoryId(), savedCategory.getName());

        return addCategoryResponse(savedCategory);
    }

    public List<CategoryResponse> getAllCategories(){
        User user  = authService.currentUser();

        log.info("Get all categories request by user {}", user.getName());
        List<Category> categories =  categoryRepository.findByUser(user);
        log.info("Successfully fetched {} categories for user {} ", categories.size(), user.getName());

        return categories.stream()
                .map(this::addCategoryResponse).toList();
    }

    @Transactional
    public CategoryResponse deleteCategory(Long id){
        User user = authService.currentUser();
        log.info("Delete category request for : {} by user {}", id, user.getEmail());

        Category req = categoryRepository.findByCategoryIdAndUser(id, user).orElseThrow(
                () -> {
                    log.warn("Category with id {} don't exists for user {}", id, user.getName());
                    return new InvalidCategoryException("Category with id " + id + " doesn't exists for user " + user.getName());
                }
        );
        CategoryResponse response = addCategoryResponse(req);
        categoryRepository.delete(req);
        log.info("Category {} deleted successfully for user {}", req.getName(), user.getName());

        return response;
    }

    private CategoryResponse addCategoryResponse(Category category){
        return new CategoryResponse(category.getUser().getId(), category.getName());
    }
}
