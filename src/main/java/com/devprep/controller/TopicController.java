package com.devprep.controller;

import com.devprep.dto.TopicRequest;
import com.devprep.dto.TopicResponse;
import com.devprep.service.TopicService;
import org.hibernate.validator.constraints.ScriptAssert;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping("/{categoryId}/topics")
    public ResponseEntity<TopicResponse> addTopic(@RequestBody TopicRequest topicRequest, @PathVariable Long categoryId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.addTopics(topicRequest, categoryId));
    }

    @PatchMapping("/{categoryId}/topics/{topicId}")
    public ResponseEntity<TopicResponse> editTopic(@RequestBody TopicRequest request, @PathVariable Long categoryId, @PathVariable Long topicId) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.editTopic(request,categoryId, topicId));
    }

    @DeleteMapping("/{categoryId}/topics/{topicId}")
    public ResponseEntity<String> deleteTopic(@PathVariable Long categoryId, @PathVariable Long topicId) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.deleteTopic(topicId, categoryId));
    }
}
