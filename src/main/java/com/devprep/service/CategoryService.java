package com.devprep.service;

import com.devprep.dto.CategoryRequest;
import com.devprep.dto.CategoryResponse;
import com.devprep.entity.Category;
import com.devprep.entity.User;
import com.devprep.exception.InvalidCategoryException;
import com.devprep.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        log.info("Add category request for : {} by user {}", request.name(), user.getId());

        if(categoryRepository.existsByNameAndUser(request.name(), user)){
            log.warn("Category with name {} already exists for user {}", request.name(), user.getId());
            throw new InvalidCategoryException("Category with name " + request.name() + " already exists for user " + user.getId());
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

        log.info("Edit category request from : {}", user.getId());
        Category req = findCategoryById(id,user);

        if(!req.getName().equalsIgnoreCase(request.name()) &&
                categoryRepository.existsByNameAndUser(request.name(), user)) {
            log.warn("Category {} already exists for user {}", request.name(), user.getId());
            throw new InvalidCategoryException("Category with name " + request.name() + " already exists for user " + user.getId());
        }

        log.info("Category edit request received for :{}", req.getName());
        req.setName(request.name());
        Category savedCategory = categoryRepository.save(req);
        log.info("Category updated for : id {} with name {}", savedCategory.getCategoryId(), savedCategory.getName());

        return addCategoryResponse(savedCategory);
    }

    public List<CategoryResponse> getAllCategories(){
        User user  = authService.currentUser();

        log.info("Get all categories request by user {}", user.getId());
        List<Category> categories =  categoryRepository.findByUser(user);
        log.info("Successfully fetched {} categories for user {} ", categories.size(), user.getId());

        return categories.stream()
                .map(this::addCategoryResponse).toList();
    }

    @Transactional
    public void deleteCategory(Long id){
        User user = authService.currentUser();
        log.info("Delete category request for : {} by user {}", id, user.getEmail());

        Category req = findCategoryById(id, user);
        categoryRepository.delete(req);
        log.info("Category {} deleted successfully for user {}", req.getName(), user.getId());
    }

    public Category findCategoryById(Long id, User user){
        return categoryRepository.findByCategoryIdAndUser(id, user).orElseThrow(
                () -> {
                    log.warn("Category with id {} don't exists for user {}", id, user.getId());
                    return new InvalidCategoryException("Category with id " + id + " doesn't exists for user " + user.getId());
                }
        );
    }

    private CategoryResponse addCategoryResponse(Category category){
        return new CategoryResponse(category.getCategoryId(), category.getName());
    }
}
