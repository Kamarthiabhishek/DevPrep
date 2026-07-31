package com.devprep.service;

import com.devprep.dto.NotesRequest;
import com.devprep.dto.NotesResponse;
import com.devprep.entity.Category;
import com.devprep.entity.Notes;
import com.devprep.entity.Topic;
import com.devprep.entity.User;
import com.devprep.repository.NotesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotesService {

    private final NotesRepository notesRepository;
    private final TopicService topicService;
    private final CategoryService categoryService;
    private final AuthService authService;

    public NotesService(NotesRepository notesRepository, AuthService authService , CategoryService categoryService, TopicService topicService) {
        this.notesRepository = notesRepository;
        this.authService = authService;
        this.categoryService = categoryService;
        this.topicService = topicService;
    }

    public NotesResponse addNotes(NotesRequest request, Long categoryId, Long topicId){
        User user = authService.currentUser();
        log.info("Add notes for category {}, topic {} received from user {}", categoryId, topicId, user.getId());

        Category category = categoryService.findCategoryById(categoryId, user);
        Topic topic = topicService.findTopicById(topicId, category,user);

        Notes notes = new Notes(
                request.content(),
                topic
        );
        log.info("Notes set for topic {} from user {}", topic.getTitle(), user.getId());

        Notes savedNotes = notesRepository.save(notes);
        return buildNotesResponse(savedNotes);
    }

    public NotesResponse buildNotesResponse(Notes notes) {
        return new NotesResponse(notes.getNotesId(), notes.getContent());
    }
}
