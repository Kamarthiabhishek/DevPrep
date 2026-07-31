package com.devprep.controller;

import com.devprep.dto.NotesRequest;
import com.devprep.dto.NotesResponse;
import com.devprep.service.NotesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @PostMapping("/{categoryId}/topics/{topicId}/notes")
    public ResponseEntity<NotesResponse> addNotes(@RequestBody NotesRequest request, @PathVariable Long categoryId, @PathVariable Long topicId) {
        return ResponseEntity.ok(notesService.addNotes(request, categoryId, topicId));
    }
}
