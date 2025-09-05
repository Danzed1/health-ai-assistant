# Health AI Model - Advanced Configuration
# This file demonstrates proper fine-tuning data format for Ollama

## Training Data Format (JSONL)
Each line should be a JSON object with this structure:
```json
{
  "messages": [
    {"role": "system", "content": "You are a health AI assistant..."},
    {"role": "user", "content": "User question"},
    {"role": "assistant", "content": "AI response"}
  ]
}
```

## Current Health Dataset Stats
- **Total Examples**: 6 health Q&A pairs
- **Topics Covered**: UTI prevention, kidney stones, blood pressure, allergies, sleep, fever
- **Format**: Question-answer pairs with medical disclaimers

## Model Configuration
- **Base Model**: llama3.2:latest (2.0 GB)
- **Custom Model**: health-ai-doctor
- **Training Method**: System prompt + few-shot learning
- **Parameters**: 
  - Temperature: 0.3 (balanced creativity/consistency)
  - Top-p: 0.9 (nucleus sampling)
  - Top-k: 40 (vocabulary limitation)
  - Repeat penalty: 1.1 (avoid repetition)

## Usage Examples

### Terminal Usage
```bash
ollama run health-ai-doctor "How can I improve my fever?"
```

### API Usage
```bash
curl http://localhost:11434/api/generate \
  -d '{
    "model": "health-ai-doctor",
    "prompt": "What are the symptoms of kidney stones?",
    "stream": false
  }'
```

### Spring Boot Integration
The model works with your existing Spring Boot health-ai-api:
- Endpoint: POST /api/v1/chat
- Model: health-ai-doctor
- No code changes needed

## Expected Responses
The model will provide exact trained responses for known questions and generate appropriate health advice for new questions, always ending with medical disclaimers.

## Model Management
- **Create**: `python create_health_model.py`
- **List**: `ollama list`
- **Test**: `ollama run health-ai-doctor "test question"`
- **Delete**: `ollama rm health-ai-doctor`
- **Update**: Delete and recreate with new data

## Files Structure
```
d:\AI_ML\Fine-Tunning\
├── health-dataset.json          # Training data (6 examples)
├── create_health_model.py       # Model creation script
├── Modelfile-health-ai         # Generated Ollama model config
├── fine_tune_llama.py          # Legacy script (can be updated)
└── UPDATE_PROCESS.md           # Update workflow documentation
```
