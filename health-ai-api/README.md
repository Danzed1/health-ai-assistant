# Health AI API

A Spring Boot application that provides a REST API for health-related AI assistance using your fine-tuned LLaMA model through Ollama.

## Features

- ✅ REST API endpoints for health queries
- ✅ Integration with local Ollama LLaMA model
- ✅ Web-based chat interface
- ✅ Health check endpoints
- ✅ Proper error handling and logging
- ✅ Session management

## Prerequisites

1. **Java 21** (as specified in pom.xml)
2. **Maven** for building
3. **Ollama running locally** with your fine-tuned model

## Quick Start

1. **Make sure Ollama is running:**
   ```bash
   ollama serve
   ```

2. **Verify your model is available:**
   ```bash
   ollama list
   ```
   (Should show `llama3.2:latest`)

3. **Build and run the Spring Boot application:**
   ```bash
   cd health-ai-api
   mvn clean install
   mvn spring-boot:run
   ```

4. **Test the API:**
   - Web Interface: http://localhost:8080
   - Health Check: http://localhost:8080/api/v1/health
   - API Info: http://localhost:8080/api/v1/info

## API Endpoints

### POST `/api/v1/chat`
Send a health-related question to the AI.

**Request:**
```json
{
  "message": "What are the symptoms of diabetes?",
  "userId": "user123",
  "sessionId": "optional-session-id"
}
```

**Response:**
```json
{
  "response": "Common symptoms of diabetes include...",
  "modelUsed": "llama3.2:latest",
  "timestamp": "2025-09-03T10:30:00",
  "responseTimeMs": 1250,
  "sessionId": "session-abc-123"
}
```

### GET `/api/v1/health`
Check if the service and Ollama are running properly.

### GET `/api/v1/info`
Get API information and available endpoints.

## Configuration

Update `application.properties` to customize:

```properties
# Ollama Configuration
ollama.base-url=http://localhost:11434
ollama.model-name=llama3.2:latest
ollama.timeout=60000

# Custom system message
health.assistant.system-message=Your custom health assistant instructions...
```

## Testing with curl

```bash
# Test the chat endpoint
curl -X POST http://localhost:8080/api/v1/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What should I do for a headache?"}'

# Check health
curl http://localhost:8080/api/v1/health
```

## Troubleshooting

1. **"Ollama is not available"**: Make sure Ollama is running (`ollama serve`)
2. **Model not found**: Verify the model name in `application.properties`
3. **Timeout errors**: Increase the timeout value in configuration
4. **Port conflicts**: Change `server.port` in `application.properties`

## Development

- **Logs**: Check console output for detailed request/response logs
- **Debug**: Set `logging.level.com.healthai=DEBUG` in application.properties
- **CORS**: Currently allows all origins - configure properly for production

## Next Steps

- Deploy to cloud platform
- Add authentication/authorization
- Implement conversation history
- Add rate limiting
- Create more sophisticated health domain logic
