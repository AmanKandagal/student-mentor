package com.studnetmentor.student_mentor_backend.controllers;

import com.studnetmentor.student_mentor_backend.models.ChatMessage;
import com.studnetmentor.student_mentor_backend.repositories.ChatMessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular to access
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;

    public ChatController(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @GetMapping("/history/{userId1}/{userId2}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(
            @PathVariable String userId1, 
            @PathVariable String userId2) {
        
        
        List<ChatMessage> history = chatMessageRepository
            .findBySenderIdAndRecipientIdOrSenderIdAndRecipientIdOrderByTimestampAsc(
                userId1, userId2, userId2, userId1
            );
            
        return ResponseEntity.ok(history);
    }
}