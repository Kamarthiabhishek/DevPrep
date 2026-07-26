package com.devprep.devprepai.service;


import com.devprep.dto.CategoryRequest;
import com.devprep.dto.CategoryResponse;
import com.devprep.entity.Category;
import com.devprep.entity.User;
import com.devprep.repository.CategoryRepository;
import com.devprep.service.AuthService;
import com.devprep.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private AuthService authService;

    @InjectMocks
    private CategoryService categoryService;


    @Test
    void shouldAddCategorySuccessfully(){

        //Arrange
        User user = new User();
        user.setId(1L);
        user.setName("Abhi");
        user.setEmail("abhi@gmail.com");

        CategoryRequest request = new CategoryRequest(1L,"JAVA");

        Category category = new Category("Java",user);
        category.setCategoryId(10L);

        when(authService.currentUser()).thenReturn(user);
        when(categoryRepository.existsByNameAndUser("JAVA", user)).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        //Act
        CategoryResponse response = categoryService.addCategory(request);

        //Assert
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("JAVA", response.name());


        verify(authService).currentUser();
        verify(categoryRepository).existsByNameAndUser("Java", user);
        verify(categoryRepository).save(any(Category.class));
    }


    @Test
    void shouldEditCategorySuccessfully(){
        //Arrange
        User user = new User();
        user.setId(1L);
        user.setName("Abhi");
        user.setEmail("abhi@gmail.com");

        CategoryRequest request = new CategoryRequest(1L,"JAVA");

    }
}
