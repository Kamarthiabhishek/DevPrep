package com.devprep.controller;

import com.devprep.dto.TopicRequest;
import com.devprep.dto.TopicResponse;
import com.devprep.dto.TopicStatusRequest;
import com.devprep.service.TopicService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping("/{categoryId}/topics")
    public ResponseEntity<TopicResponse> addTopic(@Valid @RequestBody TopicRequest topicRequest, @PathVariable Long categoryId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.addTopics(topicRequest, categoryId));
    }

    @PatchMapping("/{categoryId}/topics/{topicId}")
    public ResponseEntity<TopicResponse> editTopic(@Valid @RequestBody TopicRequest request, @PathVariable Long categoryId, @PathVariable Long topicId) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.editTopic(request,categoryId, topicId));
    }

    @DeleteMapping("/{categoryId}/topics/{topicId}")
    public ResponseEntity<String> deleteTopic(@PathVariable Long categoryId, @PathVariable Long topicId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(topicService.deleteTopic(categoryId, topicId));
    }

    @GetMapping("/{categoryId}/topics")
    public ResponseEntity<List<TopicResponse>> getAllTopicForCategory(@PathVariable Long categoryId) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.findAllTopics(categoryId));
    }

    @PatchMapping("/{categoryId}/topics/{topicId}/status")
    public ResponseEntity<TopicResponse> updateStatus(@PathVariable Long categoryId, @PathVariable Long topicId, @RequestBody TopicStatusRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(topicService.updateStatus(categoryId, topicId, request));
    }
}
