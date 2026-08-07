package com.devprep.service;

import com.devprep.dto.NotesRequest;
import com.devprep.dto.NotesResponse;
import com.devprep.entity.Category;
import com.devprep.entity.Notes;
import com.devprep.entity.Topic;
import com.devprep.entity.User;
import com.devprep.exception.InvalidNotesException;
import com.devprep.repository.NotesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
        log.info("Notes added for topic {} from user {}", topic.getTitle(), user.getId());

        Notes savedNotes = notesRepository.save(notes);
        return buildNotesResponse(savedNotes);
    }

    public List<NotesResponse> getNotesForTopic(Long categoryId, Long topicId){
        User user = authService.currentUser();
        log.info("List notes for category {}, topic {} received from user {}", categoryId, topicId, user.getId());

        Category category = categoryService.findCategoryById(categoryId, user);
        Topic topic = topicService.findTopicById(topicId, category,user);

        List<Notes> notes = notesRepository.findByTopic(topic);
        log.info("Successfully fetched {} notes for topic {} ", notes.size(),topic.getTitle());

        return notes.stream().map(this::buildNotesResponse).toList();
    }

    public NotesResponse updateNotes(NotesRequest request, Long categoryId, Long topicId, Long notesId){
        User user = authService.currentUser();
        log.info("Edit notes for category {}, topic {} received from user {}", categoryId, topicId, user.getId());

        Category category = categoryService.findCategoryById(categoryId, user);
        Topic topic = topicService.findTopicById(topicId, category,user);
        Notes notes = findNoteById(notesId, topic, user);

        notes.setContent(request.content());
        log.info("Notes updated for topic {} from user {}", topic.getTitle(), user.getId());
        Notes savedNotes = notesRepository.save(notes);
        return buildNotesResponse(savedNotes);

    }

    public String deleteNotes(Long categoryId, Long topicId, Long notesId){
        User user = authService.currentUser();
        log.info("Delete notes for category {}, topic {} received from user {}", categoryId, topicId, user.getId());

        Category category = categoryService.findCategoryById(categoryId, user);
        Topic topic = topicService.findTopicById(topicId, category,user);
        Notes notes = findNoteById(notesId, topic, user);

        log.info("Notes : {} deleted for topic {} from user {}",notes.getNotesId() ,topic.getTitle(), user.getId());
        notesRepository.delete(notes);
        return "Note deleted successfully for id : "+notes.getNotesId();

    }


    public Notes findNoteById(Long id, Topic topic, User user){
        log.info("Find notes by id {} for user {}", id, user.getId());

        return notesRepository.findByTopicAndNotesId(topic,id).orElseThrow(() ->{
            log.error("Notes with id {} not found for user {}", id, user.getId());
            return new InvalidNotesException("Notes with id " + id + " not found for user " + user.getId());
        });
    }

    public NotesResponse buildNotesResponse(Notes notes) {
        return new NotesResponse(notes.getNotesId(), notes.getContent());
    }
}
