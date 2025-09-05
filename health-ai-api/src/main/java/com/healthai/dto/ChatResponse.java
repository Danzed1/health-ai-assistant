package com.healthai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String response;
    
    @JsonProperty("model_used")
    private String modelUsed;
    
    private LocalDateTime timestamp;
    
    @JsonProperty("response_time_ms")
    private Long responseTimeMs;
    
    private String sessionId;
    
    public static ChatResponse success(String response, String modelUsed, Long responseTimeMs, String sessionId) {
        return new ChatResponse(response, modelUsed, LocalDateTime.now(), responseTimeMs, sessionId);
    }
    
    public static ChatResponse error(String errorMessage) {
        return new ChatResponse(errorMessage, "error", LocalDateTime.now(), 0L, null);
    }
}
