package com.healthai.controller;

import com.healthai.dto.ChatRequest;
import com.healthai.dto.ChatResponse;
import com.healthai.service.HealthChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*") // Configure properly for production
public class ChatController {
    
    private final HealthChatService healthChatService;
    
    @Autowired
    public ChatController(HealthChatService healthChatService) {
        this.healthChatService = healthChatService;
    }
    
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        try {
            // Validate input
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ChatResponse.error("Message cannot be empty"));
            }
            
            // Generate session ID if not provided
            String sessionId = request.getSessionId() != null ? 
                    request.getSessionId() : UUID.randomUUID().toString();

            // Get response from health service
            ChatResponse response = healthChatService.getResponse(request.getMessage(), sessionId);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error processing chat request", e);
            return ResponseEntity.internalServerError()
                    .body(ChatResponse.error("Internal server error occurred"));
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        boolean ollamaAvailable = healthChatService.isOllamaAvailable();
        
        if (ollamaAvailable) {
            return ResponseEntity.ok("Health AI API is running and Ollama is available");
        } else {
            return ResponseEntity.status(503)
                    .body("Health AI API is running but Ollama is not available");
        }
    }
    
    @GetMapping("/info")
    public ResponseEntity<Object> getApiInfo() {
        return ResponseEntity.ok(new Object() {
            public final String name = "Health AI API";
            public final String version = "1.0.0";
            public final String description = "AI-powered health assistance using fine-tuned LLaMA model";
            public final String[] endpoints = {"/api/v1/chat", "/api/v1/health", "/api/v1/info"};
        });
    }
}