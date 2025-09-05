# Health AI API

Spring Boot REST API component of the Health AI Assistant project. This module provides the backend services for the health AI chatbot using fine-tuned LLaMA 3.2 model through Ollama.

## Component Overview

This is the **backend API module** of the Health AI Assistant. For complete project documentation, see the [main README](../README.md).

### Key Features
- 🚀 **Spring Boot 3.5.5** with WebFlux reactive programming
- 🤖 **Ollama Integration** for health-ai-doctor model
- 🎨 **Web Interface** with beautiful gradient design
- 📊 **Session Management** with UUID-based tracking
- 🛡️ **Error Handling** and input validation
- 📝 **Health Monitoring** endpoints

## Quick Start

### Prerequisites
- Java 21
- Maven 3.9+
- Ollama with `health-ai-doctor` model

### Run the API
```bash
# From project root, ensure model is created first
python create_health_model.py

# Build and run API
cd health-ai-api
mvn clean spring-boot:run

# Test endpoints
curl http://localhost:8080/api/v1/health
```

### Access Points
- **Web Interface**: http://localhost:8080
- **API Health**: http://localhost:8080/api/v1/health
- **API Info**: http://localhost:8080/api/v1/info

## API Endpoints

### POST `/api/v1/chat`
Send a health question to the AI assistant.

**Request:**
```json
{
  "message": "How can I prevent urinary tract infections?",
  "sessionId": "optional-session-id"
}
```

**Response:**
```json
{
  "response": "UTI prevention includes drinking plenty of water...",
  "success": true,
  "error": null,
  "modelUsed": "health-ai-doctor",
  "responseTime": 1250,
  "sessionId": "session-abc-123",
  "timestamp": 1693910400000
}
```

### GET `/api/v1/health`
Check if the API and Ollama model are available.

### GET `/api/v1/info`
Get API version and endpoint information.

## Configuration

The API uses `application.properties` for configuration:

```properties
# Server Configuration
server.port=8080

# Ollama Configuration
ollama.base-url=http://localhost:11434
ollama.model-name=health-ai-doctor
ollama.timeout=120000

# Health Assistant Configuration
health.assistant.system-message=
```

## Testing

```bash
# Test chat endpoint
curl -X POST http://localhost:8080/api/v1/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "How can I improve my sleep quality?"}'

# Check health status
curl http://localhost:8080/api/v1/health
```

## Project Structure

```
src/
├── main/java/com/healthai/
│   ├── HealthAiApiApplication.java     # Main application
│   ├── config/                        # Configuration classes
│   ├── controller/                    # REST controllers
│   ├── dto/                          # Data transfer objects
│   └── service/                      # Business logic
└── main/resources/
    ├── application.properties        # Configuration
    └── static/index.html            # Web interface
```

## Documentation

- **[Complete Documentation](../TECHNICAL_DOCUMENTATION.md)** - Full technical details
- **[API Reference](../API_REFERENCE.md)** - Complete API documentation
- **[Deployment Guide](../DEPLOYMENT_GUIDE.md)** - Production deployment

---

This API component is part of the larger Health AI Assistant project. See the [main README](../README.md) for complete setup instructions and project overview.
