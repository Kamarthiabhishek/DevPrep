package com.devprep.service;

import com.devprep.dto.TopicRequest;
import com.devprep.dto.TopicResponse;
import com.devprep.entity.Category;
import com.devprep.entity.Topic;
import com.devprep.entity.User;
import com.devprep.enums.TopicStatus;
import com.devprep.exception.InvalidCategoryException;
import com.devprep.exception.InvalidTopicException;
import com.devprep.repository.TopicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final AuthService authService;
    private final CategoryService categoryService;


    public TopicService(
            AuthService authService, TopicRepository topicRepository, CategoryService categoryService
    ){this.topicRepository = topicRepository;
    this.authService = authService;
    this.categoryService = categoryService;
    }


    @Transactional
    public TopicResponse addTopics(TopicRequest topicRequest, Long categoryId) {
        User user = authService.currentUser();
        log.info("Add topic request received for user :  {}", user.getId());

        Category category = categoryService.findCategoryById(categoryId, user);

        if(topicRepository.existsByTitleAndCategory(topicRequest.title(), category)){
            log.info("Topic already exists for title :  {} and category : {}", topicRequest.title(), category.getName());
            throw new InvalidCategoryException("Topic already exists for category "+ category.getName());
        }

        TopicStatus status = TopicStatus.NOT_STARTED;
        Topic learningTopic = new Topic(
                topicRequest.title(),
                topicRequest.description(),
                category,
                status
        );
        Topic savedTopic = topicRepository.save(learningTopic);
        log.info("Topic saved successfully : topicId : {}", savedTopic.getTopicId());
        return topicResponse(learningTopic);
    }

    public TopicResponse editTopic(TopicRequest request, Long categoryId, Long topicId) {
        User user = authService.currentUser();
        log.info("Edit topic request received for topicId : {} user :  {}", topicId,user.getId());

        Category category = categoryService.findCategoryById(categoryId,user);
        Topic topic = findTopicById(topicId, category,user);

        if(!topic.getTitle().equalsIgnoreCase(request.title()) && topicRepository.existsByTitleAndCategory(request.title(), category)){
            log.warn("Topic with title : {} already exists for category : {}" ,request.title(), category.getName());
            throw new InvalidTopicException("Topic already exists for category "+ category.getName());
        }

        topic.setTitle(request.title());
        Topic savedTopic = topicRepository.save(topic);
        log.info("Topic updated successfully : topicId : {}", savedTopic.getTopicId());

        return topicResponse(savedTopic);
    }

    public List<TopicResponse> findAllTopics(Long categoryId) {
        User user = authService.currentUser();
        log.info("Find topic request received for category :  {} by user {}", categoryId, user.getId());
        Category category = categoryService.findCategoryById(categoryId, user);

        List<Topic> topics = topicRepository.findByCategory(category);
        return topics.stream().map(this::topicResponse).toList();
    }

    @Transactional
    public void deleteTopic(Long topicId, Long categoryId) {
        User user = authService.currentUser();
        log.info("Delete request received for topic : {} from user : {}", topicId, user.getId());

        Category category = categoryService.findCategoryById(categoryId,user);
        Topic topic = findTopicById(topicId, category, user);
        topicRepository.delete(topic);
        topicRepository.flush();

        log.info("Topic deleted successfully : topicId : {}", topic.getTopicId());
    }


    public Topic findTopicById(Long topicId, Category category, User user) {
        return topicRepository.findByTopicIdAndCategory(topicId, category).orElseThrow(
                () -> {
                    log.warn("Topic with id {} don't exists for category {} for user {}",topicId, category.getName(), user.getId());
                    return new InvalidCategoryException("Topic with id " + topicId + " doesn't exists for category " + category.getName());
                }
        );
    }

    public TopicResponse topicResponse(Topic learningTopic){
        return new TopicResponse(learningTopic.getTopicId(), learningTopic.getTitle(), learningTopic.getDescription(), learningTopic.getStatus());
    }
}
