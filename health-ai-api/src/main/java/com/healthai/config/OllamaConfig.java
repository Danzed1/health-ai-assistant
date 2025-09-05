package com.healthai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "ollama")
public class OllamaConfig {
    private String baseUrl = "http://localhost:11434";
    private String modelName = "health-ai-doctor";
    private int timeout = 120000;
}
