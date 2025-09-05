package com.healthai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChatRequest {
    private String message;
    
    @JsonProperty("user_id")
    private String userId;
    
    private String sessionId;
}
