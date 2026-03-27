import requests
import time
import json

URL = "http://localhost:11434/api/generate"
payload = {
    "model": "mistral",
    "prompt": "Say 'Ollama is working' if you can hear me.",
    "stream": False
}

print("Testing Ollama API connection...")
start = time.time()
try:
    response = requests.post(URL, json=payload, timeout=30)
    data = response.json()
    print(f"\n✅ Connection successful!")
    print(f"⏱️  Time taken: {time.time() - start:.2f} seconds")
    print(f"🤖 AI Response: {data.get('response', '')}")
except requests.exceptions.ConnectionError:
    print("\n❌ FAILED: Could not connect to http://localhost:11434")
    print("   Ollama is NOT running. You must run `brew services start ollama`")
except Exception as e:
    print(f"\n❌ Error: {e}")
