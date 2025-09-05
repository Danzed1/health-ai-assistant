# Health AI Assistant - API Reference Guide

## Table of Contents
1. [API Overview](#api-overview)
2. [Authentication](#authentication)
3. [Endpoints](#endpoints)
4. [Request/Response Formats](#requestresponse-formats)
5. [Error Handling](#error-handling)
6. [Rate Limiting](#rate-limiting)
7. [SDK and Examples](#sdk-and-examples)
8. [Testing](#testing)

## API Overview

The Health AI Assistant API provides intelligent health advice through a fine-tuned LLaMA 3.2 model. The API is built with Spring Boot and offers RESTful endpoints for health consultations.

### Base Information
- **Base URL**: `http://localhost:8080/api/v1`
- **Protocol**: HTTP/HTTPS
- **Content Type**: `application/json`
- **API Version**: v1
- **Response Format**: JSON

### Key Features
- Real-time health advice generation
- Session management for conversation tracking
- Comprehensive error handling
- Health check endpoints for monitoring
- Detailed response metadata

## Authentication

**Current Implementation**: No authentication required for development
**Production Recommendation**: Implement JWT-based authentication

### Future Authentication Header
```http
Authorization: Bearer <jwt-token>
```

## Endpoints

### 1. Chat Endpoint

#### POST /api/v1/chat
Send a health question and receive AI-generated advice.

**Request Body**:
```json
{
  "message": "string (required, max 1000 characters)",
  "sessionId": "string (optional, UUID format)"
}
```

**Response**:
```json
{
  "response": "string",
  "success": true,
  "error": null,
  "modelUsed": "health-ai-doctor",
  "responseTime": 1250,
  "sessionId": "uuid-string",
  "timestamp": 1693910400000
}
```

**cURL Example**:
```bash
curl -X POST http://localhost:8080/api/v1/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "How can I prevent urinary tract infections?",
    "sessionId": "123e4567-e89b-12d3-a456-426614174000"
  }'
```

**JavaScript Example**:
```javascript
const response = await fetch('http://localhost:8080/api/v1/chat', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    message: 'How can I improve my sleep quality?',
    sessionId: crypto.randomUUID()
  })
});

const data = await response.json();
console.log(data.response);
```

### 2. Health Check Endpoint

#### GET /api/v1/health
Check the availability of the API and underlying AI model.

**Response**:
```http
200 OK
Content-Type: text/plain

Health AI API is running and Ollama is available
```

**Error Response**:
```http
503 Service Unavailable
Content-Type: text/plain

Health AI API is running but Ollama is not available
```

**cURL Example**:
```bash
curl http://localhost:8080/api/v1/health
```

### 3. API Information Endpoint

#### GET /api/v1/info
Get information about the API version and available endpoints.

**Response**:
```json
{
  "name": "Health AI API",
  "version": "1.0.0",
  "description": "AI-powered health assistance using fine-tuned LLaMA model",
  "endpoints": [
    "/api/v1/chat",
    "/api/v1/health",
    "/api/v1/info"
  ]
}
```

**cURL Example**:
```bash
curl http://localhost:8080/api/v1/info
```

## Request/Response Formats

### Chat Request Schema
```json
{
  "type": "object",
  "properties": {
    "message": {
      "type": "string",
      "minLength": 1,
      "maxLength": 1000,
      "description": "User's health question"
    },
    "sessionId": {
      "type": "string",
      "format": "uuid",
      "description": "Optional session identifier for conversation tracking"
    }
  },
  "required": ["message"]
}
```

### Chat Response Schema
```json
{
  "type": "object",
  "properties": {
    "response": {
      "type": "string",
      "description": "AI-generated health advice"
    },
    "success": {
      "type": "boolean",
      "description": "Request success status"
    },
    "error": {
      "type": "string",
      "nullable": true,
      "description": "Error message if request failed"
    },
    "modelUsed": {
      "type": "string",
      "description": "Name of the AI model used"
    },
    "responseTime": {
      "type": "integer",
      "description": "Response generation time in milliseconds"
    },
    "sessionId": {
      "type": "string",
      "format": "uuid",
      "description": "Session identifier"
    },
    "timestamp": {
      "type": "integer",
      "description": "Response timestamp in Unix milliseconds"
    }
  }
}
```

## Error Handling

### HTTP Status Codes
- `200 OK`: Request successful
- `400 Bad Request`: Invalid request format or parameters
- `500 Internal Server Error`: Server-side error
- `503 Service Unavailable`: AI model unavailable

### Error Response Format
```json
{
  "response": null,
  "success": false,
  "error": "Human-readable error message",
  "modelUsed": null,
  "responseTime": 0,
  "sessionId": "uuid-if-provided",
  "timestamp": 1693910400000
}
```

### Common Error Scenarios

#### Empty Message
**Request**:
```json
{
  "message": ""
}
```

**Response**:
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

#### Message Too Long
**Response**:
```json
{
  "response": null,
  "success": false,
  "error": "Message too long (max 1000 characters)",
  "modelUsed": null,
  "responseTime": 0,
  "sessionId": null,
  "timestamp": 1693910400000
}
```

#### AI Model Unavailable
**Response**:
```json
{
  "response": null,
  "success": false,
  "error": "I'm sorry, I'm having trouble processing your request right now. Please try again later.",
  "modelUsed": null,
  "responseTime": 5000,
  "sessionId": "uuid-string",
  "timestamp": 1693910400000
}
```

## Rate Limiting

### Current Limits
- **Development**: No rate limiting
- **Production**: 10 requests per minute per IP (recommended)

### Rate Limit Headers (Future Implementation)
```http
X-RateLimit-Limit: 10
X-RateLimit-Remaining: 9
X-RateLimit-Reset: 1693910460
```

### Rate Limit Exceeded Response
```http
429 Too Many Requests
Content-Type: application/json

{
  "response": null,
  "success": false,
  "error": "Rate limit exceeded. Please try again later.",
  "modelUsed": null,
  "responseTime": 0,
  "sessionId": null,
  "timestamp": 1693910400000
}
```

## SDK and Examples

### JavaScript/TypeScript SDK

#### Installation
```bash
npm install axios
```

#### SDK Implementation
```typescript
// health-ai-sdk.ts
export interface ChatRequest {
  message: string;
  sessionId?: string;
}

export interface ChatResponse {
  response: string;
  success: boolean;
  error: string | null;
  modelUsed: string;
  responseTime: number;
  sessionId: string;
  timestamp: number;
}

export class HealthAIClient {
  private baseUrl: string;

  constructor(baseUrl: string = 'http://localhost:8080/api/v1') {
    this.baseUrl = baseUrl;
  }

  async chat(request: ChatRequest): Promise<ChatResponse> {
    const response = await fetch(`${this.baseUrl}/chat`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return await response.json();
  }

  async healthCheck(): Promise<boolean> {
    try {
      const response = await fetch(`${this.baseUrl}/health`);
      return response.ok;
    } catch {
      return false;
    }
  }

  async getInfo(): Promise<any> {
    const response = await fetch(`${this.baseUrl}/info`);
    return await response.json();
  }
}
```

#### Usage Example
```typescript
import { HealthAIClient } from './health-ai-sdk';

const client = new HealthAIClient();

// Send a health question
const response = await client.chat({
  message: 'How can I improve my sleep quality?',
  sessionId: crypto.randomUUID()
});

console.log(response.response);
```

### Python SDK

#### Installation
```bash
pip install requests
```

#### SDK Implementation
```python
# health_ai_client.py
import requests
import uuid
from typing import Optional, Dict, Any

class HealthAIClient:
    def __init__(self, base_url: str = "http://localhost:8080/api/v1"):
        self.base_url = base_url
        self.session = requests.Session()
        self.session.headers.update({'Content-Type': 'application/json'})

    def chat(self, message: str, session_id: Optional[str] = None) -> Dict[Any, Any]:
        """Send a health question and get AI response."""
        if session_id is None:
            session_id = str(uuid.uuid4())
        
        payload = {
            "message": message,
            "sessionId": session_id
        }
        
        response = self.session.post(f"{self.base_url}/chat", json=payload)
        response.raise_for_status()
        return response.json()

    def health_check(self) -> bool:
        """Check if the API and AI model are available."""
        try:
            response = self.session.get(f"{self.base_url}/health")
            return response.status_code == 200
        except requests.RequestException:
            return False

    def get_info(self) -> Dict[Any, Any]:
        """Get API information."""
        response = self.session.get(f"{self.base_url}/info")
        response.raise_for_status()
        return response.json()
```

#### Usage Example
```python
from health_ai_client import HealthAIClient

# Initialize client
client = HealthAIClient()

# Check if service is available
if client.health_check():
    # Send health question
    response = client.chat("How can I prevent kidney stones?")
    print(response['response'])
else:
    print("Health AI service is not available")
```

### Java SDK

```java
// HealthAIClient.java
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.UUID;

public class HealthAIClient {
    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public HealthAIClient() {
        this("http://localhost:8080/api/v1");
    }

    public HealthAIClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public ChatResponse chat(String message, String sessionId) throws Exception {
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
        }

        ChatRequest request = new ChatRequest(message, sessionId);
        String json = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/chat"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, 
            HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), ChatResponse.class);
    }

    public boolean healthCheck() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/health"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
```

## Testing

### Unit Tests

#### JavaScript/Jest
```javascript
// health-ai-client.test.js
import { HealthAIClient } from './health-ai-sdk';

describe('HealthAIClient', () => {
  const client = new HealthAIClient('http://localhost:8080/api/v1');

  test('should get health advice', async () => {
    const response = await client.chat({
      message: 'How can I improve my sleep quality?'
    });

    expect(response.success).toBe(true);
    expect(response.response).toContain('sleep');
    expect(response.modelUsed).toBe('health-ai-doctor');
  });

  test('should handle empty message', async () => {
    await expect(client.chat({ message: '' }))
      .rejects.toThrow();
  });

  test('should check health status', async () => {
    const isHealthy = await client.healthCheck();
    expect(typeof isHealthy).toBe('boolean');
  });
});
```

#### Python/pytest
```python
# test_health_ai_client.py
import pytest
from health_ai_client import HealthAIClient

class TestHealthAIClient:
    def setup_method(self):
        self.client = HealthAIClient()

    def test_chat_success(self):
        response = self.client.chat("How can I prevent UTIs?")
        
        assert response['success'] is True
        assert 'UTI' in response['response']
        assert response['modelUsed'] == 'health-ai-doctor'

    def test_chat_empty_message(self):
        with pytest.raises(requests.HTTPError):
            self.client.chat("")

    def test_health_check(self):
        is_healthy = self.client.health_check()
        assert isinstance(is_healthy, bool)

    def test_get_info(self):
        info = self.client.get_info()
        assert info['name'] == 'Health AI API'
        assert info['version'] == '1.0.0'
```

### Integration Tests

#### Postman Collection
```json
{
  "info": {
    "name": "Health AI API Tests",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Chat - UTI Prevention",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"message\": \"How can I prevent urinary tract infections?\"\n}"
        },
        "url": "{{baseUrl}}/api/v1/chat"
      },
      "test": [
        "pm.test('Status code is 200', function () {",
        "    pm.response.to.have.status(200);",
        "});",
        "",
        "pm.test('Response contains UTI advice', function () {",
        "    const response = pm.response.json();",
        "    pm.expect(response.success).to.be.true;",
        "    pm.expect(response.response).to.include('UTI');",
        "});"
      ]
    },
    {
      "name": "Health Check",
      "request": {
        "method": "GET",
        "url": "{{baseUrl}}/api/v1/health"
      },
      "test": [
        "pm.test('Health check passes', function () {",
        "    pm.response.to.have.status(200);",
        "    pm.expect(pm.response.text()).to.include('available');",
        "});"
      ]
    }
  ],
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080"
    }
  ]
}
```

### Performance Testing

#### Apache Bench (ab)
```bash
# Test chat endpoint performance
ab -n 100 -c 10 -p chat-request.json -T application/json http://localhost:8080/api/v1/chat

# chat-request.json content:
# {"message": "How can I improve my sleep quality?"}
```

#### JMeter Test Plan
```xml
<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan version="1.2">
  <hashTree>
    <TestPlan testname="Health AI API Performance Test">
      <elementProp name="TestPlan.arguments" elementType="Arguments"/>
      <boolProp name="TestPlan.functional_mode">false</boolProp>
      <boolProp name="TestPlan.serialize_threadgroups">false</boolProp>
    </TestPlan>
    <hashTree>
      <ThreadGroup testname="Chat API Load Test">
        <intProp name="ThreadGroup.num_threads">50</intProp>
        <intProp name="ThreadGroup.ramp_time">30</intProp>
        <longProp name="ThreadGroup.duration">300</longProp>
      </ThreadGroup>
    </hashTree>
  </hashTree>
</jmeterTestPlan>
```

---

## OpenAPI Specification

```yaml
openapi: 3.0.3
info:
  title: Health AI Assistant API
  description: AI-powered health assistance using fine-tuned LLaMA model
  version: 1.0.0
  contact:
    name: Health AI Support
    email: support@healthai.com
servers:
  - url: http://localhost:8080/api/v1
    description: Development server
paths:
  /chat:
    post:
      summary: Get health advice
      description: Send a health question and receive AI-generated advice
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ChatRequest'
            examples:
              uti_prevention:
                summary: UTI Prevention Question
                value:
                  message: "How can I prevent urinary tract infections?"
                  sessionId: "123e4567-e89b-12d3-a456-426614174000"
      responses:
        '200':
          description: Successful response
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ChatResponse'
        '400':
          description: Bad request
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
        '500':
          description: Internal server error
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
  /health:
    get:
      summary: Health check
      description: Check API and AI model availability
      responses:
        '200':
          description: Service is healthy
          content:
            text/plain:
              schema:
                type: string
                example: "Health AI API is running and Ollama is available"
        '503':
          description: Service unavailable
          content:
            text/plain:
              schema:
                type: string
                example: "Health AI API is running but Ollama is not available"
  /info:
    get:
      summary: API information
      description: Get API version and endpoint information
      responses:
        '200':
          description: API information
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ApiInfo'

components:
  schemas:
    ChatRequest:
      type: object
      required:
        - message
      properties:
        message:
          type: string
          minLength: 1
          maxLength: 1000
          description: User's health question
          example: "How can I improve my sleep quality?"
        sessionId:
          type: string
          format: uuid
          description: Optional session identifier
          example: "123e4567-e89b-12d3-a456-426614174000"
    
    ChatResponse:
      type: object
      properties:
        response:
          type: string
          description: AI-generated health advice
        success:
          type: boolean
          description: Request success status
        error:
          type: string
          nullable: true
          description: Error message if request failed
        modelUsed:
          type: string
          description: Name of the AI model used
        responseTime:
          type: integer
          description: Response generation time in milliseconds
        sessionId:
          type: string
          format: uuid
          description: Session identifier
        timestamp:
          type: integer
          format: int64
          description: Response timestamp in Unix milliseconds
    
    ErrorResponse:
      type: object
      properties:
        response:
          type: string
          nullable: true
          example: null
        success:
          type: boolean
          example: false
        error:
          type: string
          description: Error message
        modelUsed:
          type: string
          nullable: true
          example: null
        responseTime:
          type: integer
          example: 0
        sessionId:
          type: string
          nullable: true
        timestamp:
          type: integer
          format: int64
    
    ApiInfo:
      type: object
      properties:
        name:
          type: string
          example: "Health AI API"
        version:
          type: string
          example: "1.0.0"
        description:
          type: string
          example: "AI-powered health assistance using fine-tuned LLaMA model"
        endpoints:
          type: array
          items:
            type: string
          example: ["/api/v1/chat", "/api/v1/health", "/api/v1/info"]
```

This comprehensive API reference provides all the information needed to integrate with the Health AI Assistant API effectively.
