# Health AI Model - Update Process

## ✅ CURRENT SETUP (SIMPLIFIED)

### **Files Structure:**
```
d:\AI_ML\Fine-Tunning\
├── health-dataset.json          # Training data (6 examples)
├── create_health_model.py       # Model creation script
├── Modelfile-health-ai         # Ollama model configuration
├── UPDATE_PROCESS.md           # This file
├── README-Health-AI.md         # Documentation
└── health-ai-api/             # Spring Boot API
```

### **Method 1: Automatic Update (RECOMMENDED)**
1. Update `health-dataset.json` with your changes
2. Run the model creation script:
   ```
   C:/python/python.exe create_health_model.py
   ```
   This will:
   - Read the updated dataset
   - Generate a new `Modelfile-health-ai` with updated examples
   - Create the health-ai-doctor model automatically

### **Method 2: Manual Update (QUICK)**
1. Update `health-dataset.json`
2. Recreate the model directly:
   ```
   ollama create health-ai-doctor -f Modelfile-health-ai
   ```

### 🧪 **Testing After Updates:**
1. **Terminal Test**: `ollama run health-ai-doctor "How can I improve my fever?"`
2. **Spring Boot API Test**: POST to `http://localhost:8080/api/v1/chat`
3. **Verify**: Response matches your updated training data

### � **Model Management:**
- **List models**: `ollama list`
- **Delete model**: `ollama rm health-ai-doctor`
- **Test model**: `ollama run health-ai-doctor "test question"`

### 🚨 **Troubleshooting:**
If model doesn't respond with updated data:
1. Verify `health-dataset.json` contains your changes
2. Check `Modelfile-health-ai` was updated
3. Confirm no errors during model creation
4. Re-run: `C:/python/python.exe create_health_model.py`

### ✅ **Current Working Example:**
```
Question: "How can I improve my fever?"
Expected: "...drink plenty of water, eat pineapple, orrange..."
Status: ✅ WORKING
```
