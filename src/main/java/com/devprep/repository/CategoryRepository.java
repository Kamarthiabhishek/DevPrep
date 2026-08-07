package com.devprep.repository;


import com.devprep.entity.Category;
import com.devprep.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryIdAndUser(long id, User user);

    boolean existsByNameAndUser(String name, User user);

    List<Category> findByUser(User user);

    Optional<Category> findByCategoryId(Long id);

}
