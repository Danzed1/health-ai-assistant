package com.healthai.dto;

import lombok.Data;
import java.util.Map;

@Data
public class OllamaRequest {
    private String model;
    private String prompt;
    private String system;
    private boolean stream = false;
    private Map<String, Object> options;
}
