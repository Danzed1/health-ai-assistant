#!/usr/bin/env python3
"""
Advanced Health AI Model Creator for Ollama
Creates a properly fine-tuned health assistant model using your dataset.
"""

import json
import subprocess
import os
import sys
from pathlib import Path

def load_health_dataset(dataset_path="health-dataset.json"):
    """Load and validate the health dataset"""
    try:
        with open(dataset_path, 'r', encoding='utf-8') as f:
            dataset = []
            for line in f:
                line = line.strip()
                if line:
                    dataset.append(json.loads(line))
        
        print(f"✅ Loaded {len(dataset)} health examples from {dataset_path}")
        return dataset
    except FileNotFoundError:
        print(f"❌ Dataset file {dataset_path} not found!")
        return None
    except json.JSONDecodeError as e:
        print(f"❌ Invalid JSON in dataset: {e}")
        return None

def create_training_data(dataset):
    """Convert health dataset to Ollama training format"""
    training_data = []
    
    for item in dataset:
        # Create a proper training conversation format
        conversation = {
            "messages": [
                {
                    "role": "system",
                    "content": "You are a health AI assistant. Provide accurate, helpful health information while always including appropriate medical disclaimers. End responses with 'This is general information; consult a healthcare professional for personalized advice.'"
                },
                {
                    "role": "user", 
                    "content": item["question"]
                },
                {
                    "role": "assistant",
                    "content": item["answer"]
                }
            ]
        }
        training_data.append(conversation)
    
    return training_data

def create_modelfile(base_model="llama3.2:latest", model_name="health-ai-doctor"):
    """Create an enhanced Modelfile for health AI specialization"""
    
    # Load the health dataset
    dataset = load_health_dataset()
    if not dataset:
        return None
    
    # Create training examples in the system prompt
    training_examples = ""
    for i, example in enumerate(dataset, 1):
        training_examples += f"""
Example {i}:
Human: {example['question']}
Assistant: {example['answer']}
"""
    
    # Create comprehensive Modelfile with training data embedded
    modelfile_content = f'''FROM {base_model}

# Health AI Assistant System Configuration
SYSTEM """You are a specialized health AI assistant. You have been trained on medical knowledge and health advice.

CRITICAL INSTRUCTIONS:
1. When questions match your training examples below, provide the exact trained response
2. For new questions, provide evidence-based health information
3. Always maintain a professional, caring tone
4. Include appropriate medical disclaimers
5. End all responses with: "This is general information; consult a healthcare professional for personalized advice."

TRAINED RESPONSES - Use these exact answers for matching questions:
{training_examples}

For any other health questions, provide comprehensive, evidence-based advice following the same format and tone as your training examples."""

# Model Parameters for Health AI
PARAMETER temperature 0.3
PARAMETER top_p 0.9
PARAMETER top_k 40
PARAMETER repeat_penalty 1.1
PARAMETER num_predict 1024
'''

    # Save the Modelfile
    modelfile_path = "Modelfile-health-ai"
    with open(modelfile_path, 'w', encoding='utf-8') as f:
        f.write(modelfile_content)
    
    print(f"✅ Created {modelfile_path}")
    return modelfile_path

def create_custom_model(modelfile_path, model_name="health-ai-doctor"):
    """Create the custom health AI model using Ollama"""
    
    if not modelfile_path or not os.path.exists(modelfile_path):
        print("❌ Modelfile not found!")
        return False
    
    print(f"🚀 Creating custom model '{model_name}'...")
    print("This may take a few minutes...")
    
    try:
        # Create the model
        result = subprocess.run([
            "ollama", "create", model_name, "-f", modelfile_path
        ], capture_output=True, text=True, check=True)
        
        print(f"✅ Successfully created model '{model_name}'!")
        if result.stdout:
            print(f"Output: {result.stdout}")
        
        return True
        
    except subprocess.CalledProcessError as e:
        print(f"❌ Failed to create model: {e}")
        print(f"Error output: {e.stderr}")
        return False
    except FileNotFoundError:
        print("❌ 'ollama' command not found. Make sure Ollama is installed and in your PATH.")
        return False


def main():
    """Main function to create the health AI model"""
    
    print("🏥 Health AI Model Creator")
    print("=" * 50)
    
    # Configuration
    base_model = "llama3.2:latest"
    model_name = "health-ai-doctor"
    
    print(f"Base model: {base_model}")
    print(f"Target model: {model_name}")
    print()
    
    # Step 1: Check if base model exists
    print("1️⃣ Checking base model availability...")
    try:
        result = subprocess.run(["ollama", "list"], capture_output=True, text=True, check=True)
        if base_model not in result.stdout:
            print(f"❌ Base model '{base_model}' not found!")
            print("Please run: ollama pull llama3.2:latest")
            return
        print(f"✅ Base model '{base_model}' is available")
    except Exception as e:
        print(f"❌ Error checking models: {e}")
        return
    
    # Step 2: Create Modelfile
    print("\n2️⃣ Creating Modelfile...")
    modelfile_path = create_modelfile(base_model, model_name)
    if not modelfile_path:
        print("❌ Failed to create Modelfile")
        return
    
    # Step 3: Create custom model
    print("\n3️⃣ Creating custom model...")
    success = create_custom_model(modelfile_path, model_name)
    if not success:
        print("❌ Failed to create custom model")
        return
    
    # Step 4: Test the model
   # print("\n4️⃣ Testing the model...")
    #test_model(model_name)
    
    # Step 5: Final instructions
    print("\n" + "=" * 50)
    print("🎉 Health AI Model Created Successfully!")
    print(f"✅ Model name: {model_name}")
    print(f"✅ Based on: {base_model}")
    print(f"✅ Training data: {len(load_health_dataset())} health examples")
    print()
    print("📝 Usage Instructions:")
    print(f"   Terminal: ollama run {model_name} \"Your health question\"")
    print(f"   API: POST http://localhost:11434/api/generate")
    print(f"        {{\"model\": \"{model_name}\", \"prompt\": \"Your question\"}}")
    print()
    print("🔧 Files created:")
    print(f"   - {modelfile_path}")
    print()
    print("💡 To delete this model later:")
    print(f"   ollama rm {model_name}")

if __name__ == "__main__":
    main()
