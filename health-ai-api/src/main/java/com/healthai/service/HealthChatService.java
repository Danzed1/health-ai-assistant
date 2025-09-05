package com.healthai.service;

import com.healthai.config.OllamaConfig;
import com.healthai.dto.ChatResponse;
import com.healthai.dto.OllamaRequest;
import com.healthai.dto.OllamaResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class HealthChatService {
    
    private final WebClient webClient;
    private final OllamaConfig ollamaConfig;
    
    @Value("${health.assistant.system-message:}")
    private String systemMessage;
    
    @Autowired
    public HealthChatService(WebClient webClient, OllamaConfig ollamaConfig) {
        this.webClient = webClient;
        this.ollamaConfig = ollamaConfig;
    }
    
    public ChatResponse getResponse(String userMessage, String sessionId) {
        long startTime = System.currentTimeMillis();
        
        try {
            log.info("Processing health query: {}", userMessage.substring(0, Math.min(userMessage.length(), 50)));
            
            // Prepare request for Ollama
            OllamaRequest ollamaRequest = createOllamaRequest(userMessage);
            
            // Call Ollama API
            OllamaResponse ollamaResponse = callOllamaApi(ollamaRequest);
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            log.info("Successfully got response from Ollama in {}ms", responseTime);
            
            return ChatResponse.success(
                    ollamaResponse.getResponse(),
                    ollamaConfig.getModelName(),
                    responseTime,
                    sessionId
            );
            
        } catch (Exception e) {
            log.error("Error getting response from Ollama: ", e);
            return ChatResponse.error("I'm sorry, I'm having trouble processing your request right now. Please try again later.");
        }
    }
    
    private OllamaRequest createOllamaRequest(String userMessage) {
        OllamaRequest request = new OllamaRequest();
        request.setModel(ollamaConfig.getModelName());
        request.setPrompt(userMessage);
        
        // Only set system message if it's configured and not empty
        // This allows the fine-tuned model to use its own system prompt
        if (systemMessage != null && !systemMessage.trim().isEmpty()) {
            request.setSystem(systemMessage);
            log.debug("Using custom system message");
        } else {
            log.debug("Using fine-tuned model's built-in system prompt");
        }
        
        request.setStream(false);
        
        // Set model parameters optimized for comprehensive health responses
        Map<String, Object> options = new HashMap<>();
        options.put("temperature", 0.7);
        options.put("top_p", 0.9);
        options.put("top_k", 40);
        options.put("num_predict", 1024); // Allow longer responses for comprehensive health advice
        options.put("num_ctx", 4096); // Increase context window for better understanding
        options.put("repeat_penalty", 1.1);
        request.setOptions(options);
        
        return request;
    }
    
    private OllamaResponse callOllamaApi(OllamaRequest request) {
        String apiUrl = ollamaConfig.getBaseUrl() + "/api/generate";
        
        log.debug("Calling Ollama API at: {}", apiUrl);
        log.debug("Request payload: model={}, prompt length={}", request.getModel(), 
                request.getPrompt() != null ? request.getPrompt().length() : 0);
        
        return webClient.post()
                .uri(apiUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OllamaResponse.class)
                .timeout(Duration.ofMillis(ollamaConfig.getTimeout()))
                .retry(2) // Retry up to 2 times on failure
                .doOnSuccess(response -> log.debug("Successfully received response from Ollama: {} characters", 
                        response.getResponse() != null ? response.getResponse().length() : 0))
                .doOnError(WebClientResponseException.class, ex -> 
                    log.error("Ollama API HTTP error: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString()))
                .onErrorResume(java.util.concurrent.TimeoutException.class, ex -> {
                    log.error("Ollama API timeout after {}ms: {}", ollamaConfig.getTimeout(), ex.getMessage());
                    return Mono.error(new RuntimeException("The AI model is taking too long to respond. Please try a shorter question or try again later."));
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("Error calling Ollama API: {}", ex.getMessage());
                    if (ex.getMessage().contains("Connection refused")) {
                        return Mono.error(new RuntimeException("Cannot connect to AI model. Please ensure Ollama is running."));
                    }
                    return Mono.error(new RuntimeException("AI model temporarily unavailable: " + ex.getMessage()));
                })
                .block();
    }
    
    public boolean isOllamaAvailable() {
        try {
            // Use a simple, fast test request
            OllamaRequest testRequest = new OllamaRequest();
            testRequest.setModel(ollamaConfig.getModelName());
            testRequest.setPrompt("Hi");
            testRequest.setStream(false);
            
            // Set options for fastest possible response
            Map<String, Object> options = new HashMap<>();
            options.put("num_predict", 5); // Very short response
            options.put("temperature", 0.1); // More deterministic
            testRequest.setOptions(options);
            
            String apiUrl = ollamaConfig.getBaseUrl() + "/api/generate";
            
            webClient.post()
                    .uri(apiUrl)
                    .bodyValue(testRequest)
                    .retrieve()
                    .bodyToMono(OllamaResponse.class)
                    .timeout(Duration.ofSeconds(10)) // Short timeout for health check
                    .block();
            
            return true;
        } catch (Exception e) {
            log.warn("Ollama health check failed: {}", e.getMessage());
            return false;
        }
    }
}