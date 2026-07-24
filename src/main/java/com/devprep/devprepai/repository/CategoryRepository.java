package com.devprep.devprepai.repository;


import com.devprep.devprepai.entity.Category;
import com.devprep.devprepai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

    Category findByNameAndUser(String name, User user);
    Optional<Category> findByCategoryIdAndUser(long id, User user);

    boolean existsByNameAndUser(String name, User user);

    List<Category> findByUser(User user);

    Category deleteByCategoryId(long id);
}
