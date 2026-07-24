package com.devprep.devprepai.repository;


import com.devprep.devprepai.entity.Category;
import com.devprep.devprepai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    Boolean existsByNameAndUser(String name, User user);
    
}
