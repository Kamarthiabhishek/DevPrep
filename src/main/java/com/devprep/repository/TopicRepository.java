package com.devprep.repository;

import com.devprep.dto.TopicResponse;
import com.devprep.entity.Category;
import com.devprep.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByTopicIdAndCategory(Long topicId, Category category);
    Boolean existsByTitleAndCategory(String title, Category category);

    List<Topic> findByCategory(Category category);
}
