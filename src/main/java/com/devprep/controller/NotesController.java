package com.devprep.controller;

import com.devprep.dto.NotesRequest;
import com.devprep.dto.NotesResponse;
import com.devprep.service.NotesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping("/{categoryId}/topics/{topicId}/notes")
    public ResponseEntity<NotesResponse> addNotes(@Valid @RequestBody NotesRequest request, @PathVariable Long categoryId, @PathVariable Long topicId) {
        return ResponseEntity.ok(notesService.addNotes(request, categoryId, topicId));
    }

    @GetMapping("/{categoryId}/topics/{topicId}/notes")
    public ResponseEntity<List<NotesResponse>> getNotesForTopic(@PathVariable Long categoryId, @PathVariable Long topicId) {
        return ResponseEntity.ok(notesService.getNotesForTopic(categoryId, topicId));
    }

    @PatchMapping("/{categoryId}/topics/{topicId}/notes/{id}")
    public ResponseEntity<NotesResponse> editNotes(@PathVariable Long categoryId, @PathVariable Long topicId,@PathVariable Long id, @Valid @RequestBody NotesRequest request ){
        return ResponseEntity.ok(notesService.updateNotes(request, categoryId, topicId, id));
    }

    @DeleteMapping("/{categoryId}/topics/{topicId}/notes/{id}")
    public ResponseEntity<String> deleteNotes(@PathVariable Long categoryId, @PathVariable Long topicId,@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(notesService.deleteNotes(categoryId, topicId, id));
    }
}
