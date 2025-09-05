package com.healthai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OllamaResponse {
    private String model;
    
    @JsonProperty("created_at")
    private String createdAt;
    
    private String response;
    
    private boolean done;
    
    @JsonProperty("done_reason")
    private String doneReason;
    
    private List<Integer> context;
    
    @JsonProperty("total_duration")
    private Long totalDuration;
    
    @JsonProperty("load_duration")
    private Long loadDuration;
    
    @JsonProperty("prompt_eval_count")
    private Long promptEvalCount;
    
    @JsonProperty("prompt_eval_duration")
    private Long promptEvalDuration;
    
    @JsonProperty("eval_count")
    private Long evalCount;
    
    @JsonProperty("eval_duration")
    private Long evalDuration;
}
