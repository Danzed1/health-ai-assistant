# Health AI Assistant - Technical Documentation

## Table of Contents
1. [System Overview](#system-overview)
2. [Architecture](#architecture)
3. [Technology Stack](#technology-stack)
4. [Project Structure](#project-structure)
5. [Component Documentation](#component-documentation)
6. [API Documentation](#api-documentation)
7. [Configuration](#configuration)
8. [Database Schema](#database-schema)
9. [Security Considerations](#security-considerations)
10. [Performance Optimization](#performance-optimization)
11. [Error Handling](#error-handling)
12. [Monitoring and Logging](#monitoring-and-logging)

## System Overview

The Health AI Assistant is a full-stack application that provides intelligent health advice through a fine-tuned Large Language Model (LLaMA 3.2) running on Ollama. The system consists of three main components:

- **Custom AI Model**: Fine-tuned `health-ai-doctor` model based on LLaMA 3.2
- **Spring Boot API**: RESTful backend service for model integration
- **Web Frontend**: Modern responsive UI for user interactions

### Key Features
- ✅ Fine-tuned health AI with 6 specialized training examples
- ✅ Beautiful responsive web interface with gradient design
- ✅ RESTful API with comprehensive error handling
- ✅ Real-time health advice generation
- ✅ Session management and request tracking
- ✅ Health check endpoints for monitoring
- ✅ Smart message formatting with health-specific styling

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Web Frontend  │    │  Spring Boot    │    │   Ollama API    │
│    (HTML/JS)    │◄──►│      API        │◄──►│  health-ai-     │
│                 │    │                 │    │     doctor      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
        │                        │                        │
        │                        │                        │
    User Browser            REST API              Fine-tuned Model
                         (Port 8080)              (Port 11434)
```

### Component Interaction Flow
1. **User Input**: User enters health question in web interface
2. **API Request**: Frontend sends POST request to `/api/v1/chat`
3. **Model Processing**: Spring Boot forwards request to Ollama API
4. **AI Response**: Fine-tuned model generates health advice
5. **Response Formatting**: Backend processes and returns structured response
6. **UI Display**: Frontend formats and displays response with health styling

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.5.5
- **Java Version**: 21 (LTS)
- **Build Tool**: Apache Maven 3.9.9
- **Web Framework**: Spring WebFlux (Reactive)
- **Configuration**: Spring Boot Configuration Properties
- **Logging**: SLF4J with Logback

### AI/ML
- **Base Model**: LLaMA 3.2 (2.0 GB)
- **Model Runtime**: Ollama (localhost:11434)
- **Fine-tuning**: System prompt + few-shot learning
- **Model Name**: `health-ai-doctor`

### Frontend
- **Technology**: Vanilla HTML5, CSS3, JavaScript (ES6+)
- **Icons**: Font Awesome 6.4.0
- **Styling**: CSS Grid, Flexbox, Gradients
- **Responsiveness**: Mobile-first design

### Development Tools
- **IDE**: IntelliJ IDEA (configured)
- **Version Control**: Git (assumed)
- **Package Manager**: Maven
- **Testing**: JUnit 5, Spring Boot Test

## Project Structure

```
d:\AI_ML\Fine-Tunning\
├── health-dataset.json              # Training data (6 Q&A pairs)
├── create_health_model.py           # Model creation/update script
├── Modelfile-health-ai             # Ollama model configuration
├── fine_tune_llama.py              # Legacy fine-tuning script
├── README-Health-AI.md             # Model documentation
├── TECHNICAL_DOCUMENTATION.md      # This file
├── DEPLOYMENT_GUIDE.md             # Deployment instructions
└── health-ai-api/                  # Spring Boot application
    ├── pom.xml                     # Maven configuration
    ├── README.md                   # API documentation
    ├── src/
    │   ├── main/
    │   │   ├── java/com/healthai/
    │   │   │   ├── HealthAiApiApplication.java     # Main application
    │   │   │   ├── config/
    │   │   │   │   ├── OllamaConfig.java          # Ollama configuration
    │   │   │   │   └── WebClientConfig.java       # HTTP client config
    │   │   │   ├── controller/
    │   │   │   │   └── ChatController.java        # REST endpoints
    │   │   │   ├── dto/
    │   │   │   │   ├── ChatRequest.java           # Request models
    │   │   │   │   ├── ChatResponse.java          # Response models
    │   │   │   │   ├── OllamaRequest.java         # Ollama API request
    │   │   │   │   └── OllamaResponse.java        # Ollama API response
    │   │   │   └── service/
    │   │   │       └── HealthChatService.java     # Business logic
    │   │   └── resources/
    │   │       ├── application.properties         # Configuration
    │   │       └── static/
    │   │           └── index.html                 # Web interface
    │   └── test/
    │       └── java/com/healthai/
    │           └── HealthAiApiApplicationTests.java
    └── target/                     # Compiled artifacts
        ├── classes/
        ├── test-classes/
        └── health-ai-api-0.0.1-SNAPSHOT.jar
```

## Component Documentation

### 1. AI Model Components

#### health-dataset.json
**Purpose**: Training data for the health AI model
**Format**: JSON Lines (one JSON object per line)
**Structure**:
```json
{
  "question": "User question about health",
  "answer": "Comprehensive health advice with medical disclaimer"
}
```

**Current Topics**:
- UTI prevention
- Kidney stone symptoms
- Blood pressure management
- Allergic reactions
- Sleep quality improvement
- Fever management

#### create_health_model.py
**Purpose**: Automated model creation and updating
**Key Functions**:
- `load_health_dataset()`: Loads and validates training data
- `create_training_data()`: Converts to Ollama format
- `create_modelfile()`: Generates Ollama Modelfile
- `create_health_model()`: Creates/updates the AI model

**Parameters**:
```python
temperature = 0.3      # Balance creativity/consistency
top_p = 0.9           # Nucleus sampling
top_k = 40            # Vocabulary limitation
repeat_penalty = 1.1   # Avoid repetition
num_predict = 1024    # Max response tokens
```

#### Modelfile-health-ai
**Purpose**: Ollama model configuration with embedded training
**Key Sections**:
- System prompt with specialized health instructions
- 6 embedded training examples
- Model parameters for health-optimized responses
- Medical disclaimer requirements

### 2. Spring Boot Backend

#### HealthAiApiApplication.java
**Purpose**: Main Spring Boot application entry point
**Configuration**: 
- Spring Boot auto-configuration
- Component scanning for `com.healthai` package
- Embedded Tomcat server on port 8080

#### OllamaConfig.java
**Purpose**: Ollama integration configuration
**Properties**:
```java
@ConfigurationProperties(prefix = "ollama")
public class OllamaConfig {
    private String baseUrl = "http://localhost:11434";
    private String modelName = "health-ai-doctor";
    private int timeout = 120000; // 2 minutes
}
```

#### WebClientConfig.java
**Purpose**: HTTP client configuration for Ollama API
**Features**:
- Reactive WebClient setup
- Timeout configuration
- Connection pooling
- Error handling setup

#### ChatController.java
**Purpose**: REST API endpoints for health chat
**Endpoints**:
- `POST /api/v1/chat`: Main chat endpoint
- `GET /api/v1/health`: Health check endpoint
- `GET /api/v1/info`: API information endpoint

**Request/Response Flow**:
```java
@PostMapping("/chat")
public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
    // 1. Validate input
    // 2. Generate session ID
    // 3. Call HealthChatService
    // 4. Return structured response
}
```

#### HealthChatService.java
**Purpose**: Core business logic for health chat
**Key Methods**:
- `getResponse()`: Main processing method
- `createOllamaRequest()`: Prepares Ollama API request
- `callOllamaApi()`: Communicates with Ollama
- `isOllamaAvailable()`: Health check for Ollama

**Request Configuration**:
```java
Map<String, Object> options = new HashMap<>();
options.put("temperature", 0.7);
options.put("top_p", 0.9);
options.put("top_k", 40);
options.put("num_predict", 1024);  // Comprehensive responses
options.put("num_ctx", 4096);      // Large context window
options.put("repeat_penalty", 1.1);
```

### 3. Data Transfer Objects (DTOs)

#### ChatRequest.java
```java
public class ChatRequest {
    private String message;      // User's health question
    private String sessionId;    // Optional session identifier
    // getters, setters, validation
}
```

#### ChatResponse.java
```java
public class ChatResponse {
    private String response;     // AI-generated health advice
    private boolean success;     // Request success status
    private String error;        // Error message if any
    private String modelUsed;    // Model name for transparency
    private long responseTime;   // Performance metrics
    private String sessionId;    // Session tracking
    private long timestamp;      // Response timestamp
}
```

#### OllamaRequest.java
```java
public class OllamaRequest {
    private String model;        // Model name (health-ai-doctor)
    private String prompt;       // User question
    private String system;       // System message (optional)
    private boolean stream;      // Streaming response (false)
    private Map<String, Object> options; // Model parameters
}
```

### 4. Frontend Components

#### index.html
**Purpose**: Complete single-page web application
**Key Features**:
- Responsive CSS Grid layout
- Health-themed gradient design
- Font Awesome icons integration
- Smart message formatting
- Real-time chat interface
- Error handling and loading states

**CSS Classes**:
```css
.health-list-item     /* Formatted health advice lists */
.health-tip          /* Health tip highlighting */
.emergency-notice    /* Emergency information styling */
.ai-message          /* AI response styling */
.user-message        /* User input styling */
```

**JavaScript Functions**:
```javascript
async function sendMessage()        // Main chat function
function formatHealthResponse()     // Response formatting
function addMessage()              // UI message handling
function showTypingIndicator()     // Loading states
function clearChat()               // Chat management
```

## API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Endpoints

#### POST /chat
**Description**: Send a health question and receive AI advice
**Request Body**:
```json
{
  "message": "How can I prevent urinary tract infections?",
  "sessionId": "optional-session-id"
}
```

**Response**:
```json
{
  "response": "UTI prevention includes drinking plenty of water...",
  "success": true,
  "error": null,
  "modelUsed": "health-ai-doctor",
  "responseTime": 1250,
  "sessionId": "uuid-session-id",
  "timestamp": 1693910400000
}
```

**Error Response**:
```json
{
  "response": null,
  "success": false,
  "error": "Message cannot be empty",
  "modelUsed": null,
  "responseTime": 0,
  "sessionId": null,
  "timestamp": 1693910400000
}
```

#### GET /health
**Description**: Check API and Ollama availability
**Response**: 
- `200 OK`: "Health AI API is running and Ollama is available"
- `503 Service Unavailable`: "Health AI API is running but Ollama is not available"

#### GET /info
**Description**: Get API information
**Response**:
```json
{
  "name": "Health AI API",
  "version": "1.0.0",
  "description": "AI-powered health assistance using fine-tuned LLaMA model",
  "endpoints": ["/api/v1/chat", "/api/v1/health", "/api/v1/info"]
}
```

## Configuration

### application.properties
```properties
# Application Configuration
spring.application.name=health-ai-api
server.port=8080

# Ollama Configuration
ollama.base-url=http://localhost:11434
ollama.model-name=health-ai-doctor
ollama.timeout=120000

# Health Assistant Configuration
health.assistant.system-message=

# Logging Configuration
logging.level.root=INFO
logging.level.com.healthai=DEBUG
```

### Environment Variables
```bash
# Optional overrides
OLLAMA_BASE_URL=http://localhost:11434
OLLAMA_MODEL_NAME=health-ai-doctor
OLLAMA_TIMEOUT=120000
SERVER_PORT=8080
```

### Maven Configuration (pom.xml)
**Key Dependencies**:
- Spring Boot Starter Web
- Spring Boot Starter WebFlux (for Ollama API)
- Spring Boot DevTools (development)
- Lombok (boilerplate reduction)
- Spring Boot Starter Test (testing)

**Java Version**: 21 (LTS)
**Spring Boot Version**: 3.5.5

## Database Schema

**Note**: This application currently uses in-memory session management. No persistent database is required for basic functionality.

**Future Enhancement**: Consider adding PostgreSQL/MySQL for:
- Conversation history
- User sessions
- Analytics data
- Model performance metrics

## Security Considerations

### Current Implementation
- ✅ Input validation for chat messages
- ✅ CORS configuration (currently allows all origins)
- ✅ Error handling without sensitive information exposure
- ✅ Request timeout protection
- ✅ Medical disclaimer inclusion

### Production Recommendations
- 🔧 Implement proper CORS configuration
- 🔧 Add authentication/authorization
- 🔧 Implement rate limiting
- 🔧 Add input sanitization
- 🔧 Secure API endpoints with HTTPS
- 🔧 Add request/response logging for audit

### Medical Safety
- ✅ All responses include medical disclaimers
- ✅ Model trained to recommend professional consultation
- ✅ No diagnostic claims or medical advice
- ✅ Clear limitation statements

## Performance Optimization

### Current Optimizations
- **Reactive Programming**: WebFlux for non-blocking I/O
- **Connection Pooling**: WebClient with connection management
- **Response Timeouts**: 120-second timeout for model responses
- **Model Parameters**: Optimized for quality vs. speed balance

### Response Time Metrics
- **Average Response Time**: 1-3 seconds
- **Model Loading**: One-time startup cost
- **Memory Usage**: ~2GB for base model + JVM overhead

### Scaling Recommendations
- **Horizontal Scaling**: Multiple Spring Boot instances
- **Load Balancing**: Nginx/Apache in front of application
- **Caching**: Redis for common health questions
- **Model Optimization**: Consider smaller models for faster responses

## Error Handling

### Exception Hierarchy
```java
// Custom exceptions for specific scenarios
public class OllamaApiException extends RuntimeException
public class HealthChatException extends RuntimeException
public class ValidationException extends RuntimeException
```

### Error Response Format
```json
{
  "success": false,
  "error": "Human-readable error message",
  "response": null,
  "timestamp": 1693910400000,
  "sessionId": "session-id-if-available"
}
```

### Common Error Scenarios
1. **Ollama Not Available**: Service returns 503 with appropriate message
2. **Model Not Found**: Error indicating model needs to be created
3. **Timeout Errors**: Request exceeded 120-second limit
4. **Invalid Input**: Empty or malformed request messages
5. **Network Issues**: Connection problems with Ollama API

## Monitoring and Logging

### Logging Configuration
```properties
# Root level logging
logging.level.root=INFO

# Application-specific logging
logging.level.com.healthai=DEBUG

# Enable request/response logging for debugging
logging.level.reactor.netty=DEBUG
```

### Key Log Events
- 📝 Incoming chat requests with message preview
- 📝 Ollama API calls with timing
- 📝 Model responses with token counts
- 📝 Error conditions with stack traces
- 📝 Performance metrics (response times)

### Health Check Endpoints
- **Application Health**: `/api/v1/health`
- **Spring Boot Actuator**: Can be added for detailed metrics
- **Model Availability**: Automatic Ollama connectivity check

### Metrics to Monitor
- Response time percentiles (P50, P95, P99)
- Error rates by endpoint
- Ollama API availability
- Memory usage and garbage collection
- Request volume and patterns

---

## Development Guidelines

### Code Style
- Follow Spring Boot best practices
- Use Lombok for boilerplate reduction
- Implement proper separation of concerns
- Write comprehensive error handling
- Include logging for debugging

### Testing Strategy
- Unit tests for service layer
- Integration tests for controllers
- Mock Ollama API for testing
- Test error scenarios
- Validate response formatting

### Version Control
- Feature branch workflow
- Descriptive commit messages
- Tag releases with semantic versioning
- Document breaking changes
