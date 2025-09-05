# Health AI Assistant - Deployment Guide

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Environment Setup](#environment-setup)
3. [Local Development Setup](#local-development-setup)
4. [Production Deployment](#production-deployment)
5. [Docker Deployment](#docker-deployment)
6. [Cloud Deployment](#cloud-deployment)
7. [Monitoring and Maintenance](#monitoring-and-maintenance)
8. [Troubleshooting](#troubleshooting)
9. [Security Hardening](#security-hardening)
10. [Backup and Recovery](#backup-and-recovery)

## Prerequisites

### System Requirements
- **Operating System**: Windows 10/11, Linux (Ubuntu 20.04+), or macOS 12+
- **RAM**: Minimum 8GB (16GB recommended for production)
- **Storage**: 10GB free space for models and application
- **CPU**: Multi-core processor (4+ cores recommended)
- **Network**: Internet connection for initial model download

### Software Requirements
- **Java**: OpenJDK 21 (LTS) or Oracle JDK 21
- **Maven**: Apache Maven 3.9.9+
- **Ollama**: Latest version (for AI model runtime)
- **Git**: For source code management
- **PowerShell/Bash**: For deployment scripts

### Hardware Recommendations

#### Development Environment
- **RAM**: 8GB minimum, 16GB recommended
- **CPU**: 4-core processor
- **Storage**: SSD with 10GB free space

#### Production Environment
- **RAM**: 16GB minimum, 32GB recommended
- **CPU**: 8-core processor or better
- **Storage**: SSD with 50GB free space
- **Network**: High-speed internet connection

## Environment Setup

### 1. Java Installation

#### Windows (using Chocolatey)
```powershell
# Install Chocolatey package manager
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Install OpenJDK 21
choco install openjdk21
```

#### Windows (Manual Installation)
1. Download OpenJDK 21 from [https://adoptium.net/](https://adoptium.net/)
2. Run the installer and follow instructions
3. Set `JAVA_HOME` environment variable
4. Add `%JAVA_HOME%\bin` to PATH

#### Linux (Ubuntu/Debian)
```bash
# Update package list
sudo apt update

# Install OpenJDK 21
sudo apt install openjdk-21-jdk

# Verify installation
java -version
javac -version
```

#### Verify Java Installation
```bash
java -version
# Output should show: openjdk version "21.0.x"
```

### 2. Maven Installation

#### Windows (using Chocolatey)
```powershell
choco install maven
```

#### Windows (Manual Installation)
1. Download Maven from [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
2. Extract to `C:\apache-maven-3.9.9`
3. Add `C:\apache-maven-3.9.9\bin` to PATH
4. Set `M2_HOME` to `C:\apache-maven-3.9.9`

#### Linux (Ubuntu/Debian)
```bash
sudo apt install maven
```

#### Verify Maven Installation
```bash
mvn -version
# Output should show: Apache Maven 3.9.9
```

### 3. Ollama Installation

#### Windows
```powershell
# Download and install Ollama
winget install Ollama.Ollama

# Alternative: Download from https://ollama.ai/download/windows
```

#### Linux
```bash
# Install Ollama
curl -fsSL https://ollama.ai/install.sh | sh

# Start Ollama service
ollama serve
```

#### macOS
```bash
# Install using Homebrew
brew install ollama

# Alternative: Download from https://ollama.ai/download/mac
```

#### Verify Ollama Installation
```bash
ollama --version
# Output should show: ollama version x.x.x
```

## Local Development Setup

### 1. Clone the Repository
```bash
# Clone your repository (replace with actual URL)
git clone <your-repository-url>
cd Fine-Tunning
```

### 2. Set Up AI Model

#### Create the Health AI Model
```bash
# Navigate to the Fine-Tunning directory
cd d:\AI_ML\Fine-Tunning

# Run the model creation script
python create_health_model.py
```

#### Verify Model Creation
```bash
# List available models
ollama list

# Test the model
ollama run health-ai-doctor "How can I prevent urinary tract infections?"
```

### 3. Configure Application

#### Update application.properties
```properties
# d:\AI_ML\Fine-Tunning\health-ai-api\src\main\resources\application.properties

# Server Configuration
server.port=8080

# Ollama Configuration
ollama.base-url=http://localhost:11434
ollama.model-name=health-ai-doctor
ollama.timeout=120000

# Logging
logging.level.root=INFO
logging.level.com.healthai=DEBUG
```

### 4. Build and Run Application

#### Using Maven
```bash
# Navigate to Spring Boot application directory
cd health-ai-api

# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package application
mvn package

# Run the application
mvn spring-boot:run
```

#### Using Java JAR
```bash
# Run the packaged JAR
java -jar target/health-ai-api-0.0.1-SNAPSHOT.jar
```

### 5. Verify Deployment

#### Check Application Health
```bash
# Test health endpoint
curl http://localhost:8080/api/v1/health

# Test API info
curl http://localhost:8080/api/v1/info

# Test chat endpoint
curl -X POST http://localhost:8080/api/v1/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "How can I improve my sleep quality?"}'
```

#### Access Web Interface
Open browser and navigate to: `http://localhost:8080`

## Production Deployment

### 1. Production Environment Setup

#### System Configuration
```bash
# Create application user
sudo useradd -r -s /bin/false healthai

# Create application directories
sudo mkdir -p /opt/healthai
sudo mkdir -p /var/log/healthai
sudo mkdir -p /etc/healthai

# Set permissions
sudo chown -R healthai:healthai /opt/healthai
sudo chown -R healthai:healthai /var/log/healthai
sudo chown -R healthai:healthai /etc/healthai
```

#### Install Dependencies
```bash
# Install Java 21
sudo apt update
sudo apt install openjdk-21-jdk

# Install Ollama
curl -fsSL https://ollama.ai/install.sh | sh

# Configure Ollama as system service
sudo systemctl enable ollama
sudo systemctl start ollama
```

### 2. Application Deployment

#### Copy Application Files
```bash
# Copy JAR file
sudo cp health-ai-api-0.0.1-SNAPSHOT.jar /opt/healthai/

# Copy configuration
sudo cp application-prod.properties /etc/healthai/application.properties

# Copy model files
sudo cp health-dataset.json /opt/healthai/
sudo cp create_health_model.py /opt/healthai/
sudo cp Modelfile-health-ai /opt/healthai/
```

#### Production Configuration
```properties
# /etc/healthai/application.properties

# Server Configuration
server.port=8080
server.address=0.0.0.0

# Ollama Configuration
ollama.base-url=http://localhost:11434
ollama.model-name=health-ai-doctor
ollama.timeout=120000

# Production Logging
logging.level.root=WARN
logging.level.com.healthai=INFO
logging.file.name=/var/log/healthai/application.log
logging.file.max-size=10MB
logging.file.max-history=30

# JVM Options
spring.jpa.show-sql=false
management.endpoints.web.exposure.include=health,info,metrics
```

### 3. Create System Service

#### Create Service File
```bash
sudo tee /etc/systemd/system/healthai.service > /dev/null <<EOF
[Unit]
Description=Health AI Assistant
After=network.target ollama.service
Requires=ollama.service

[Service]
Type=simple
User=healthai
Group=healthai
WorkingDirectory=/opt/healthai
ExecStart=/usr/bin/java -jar -Dspring.config.location=/etc/healthai/application.properties /opt/healthai/health-ai-api-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=healthai

# JVM Configuration
Environment="JAVA_OPTS=-Xmx2g -Xms1g -XX:+UseG1GC"

[Install]
WantedBy=multi-user.target
EOF
```

#### Enable and Start Service
```bash
# Reload systemd
sudo systemctl daemon-reload

# Enable service
sudo systemctl enable healthai

# Start service
sudo systemctl start healthai

# Check status
sudo systemctl status healthai
```

### 4. Reverse Proxy Setup (Nginx)

#### Install Nginx
```bash
sudo apt install nginx
```

#### Configure Nginx
```bash
sudo tee /etc/nginx/sites-available/healthai > /dev/null <<EOF
server {
    listen 80;
    server_name your-domain.com;

    # Redirect HTTP to HTTPS
    return 301 https://\$server_name\$request_uri;
}

server {
    listen 443 ssl http2;
    server_name your-domain.com;

    # SSL Configuration (configure with your certificates)
    ssl_certificate /etc/ssl/certs/your-cert.pem;
    ssl_certificate_key /etc/ssl/private/your-key.pem;

    # Security headers
    add_header X-Frame-Options DENY;
    add_header X-Content-Type-Options nosniff;
    add_header X-XSS-Protection "1; mode=block";

    # Proxy to Spring Boot application
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
        
        # Timeout settings for AI responses
        proxy_read_timeout 300s;
        proxy_connect_timeout 75s;
    }

    # Health check endpoint
    location /health {
        proxy_pass http://localhost:8080/api/v1/health;
        access_log off;
    }
}
EOF
```

#### Enable Site
```bash
sudo ln -s /etc/nginx/sites-available/healthai /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

## Docker Deployment

### 1. Create Dockerfile

#### Application Dockerfile
```dockerfile
# d:\AI_ML\Fine-Tunning\health-ai-api\Dockerfile

FROM openjdk:21-jdk-slim

# Install dependencies
RUN apt-get update && apt-get install -y \
    curl \
    python3 \
    python3-pip \
    && rm -rf /var/lib/apt/lists/*

# Create application user
RUN useradd -r -s /bin/false healthai

# Set working directory
WORKDIR /app

# Copy application files
COPY target/health-ai-api-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.properties application.properties

# Copy model files
COPY ../health-dataset.json .
COPY ../create_health_model.py .
COPY ../Modelfile-health-ai .

# Set permissions
RUN chown -R healthai:healthai /app

# Switch to application user
USER healthai

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/api/v1/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 2. Docker Compose Setup

#### docker-compose.yml
```yaml
# d:\AI_ML\Fine-Tunning\docker-compose.yml

version: '3.8'

services:
  ollama:
    image: ollama/ollama:latest
    container_name: healthai-ollama
    ports:
      - "11434:11434"
    volumes:
      - ollama_data:/root/.ollama
    environment:
      - OLLAMA_KEEP_ALIVE=24h
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:11434/api/tags"]
      interval: 30s
      timeout: 10s
      retries: 3

  health-ai-api:
    build:
      context: ./health-ai-api
      dockerfile: Dockerfile
    container_name: healthai-api
    ports:
      - "8080:8080"
    depends_on:
      ollama:
        condition: service_healthy
    environment:
      - OLLAMA_BASE_URL=http://ollama:11434
      - OLLAMA_MODEL_NAME=health-ai-doctor
      - SPRING_PROFILES_ACTIVE=docker
    volumes:
      - ./logs:/var/log/healthai
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/api/v1/health"]
      interval: 30s
      timeout: 10s
      retries: 3

  nginx:
    image: nginx:alpine
    container_name: healthai-nginx
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
      - ./ssl:/etc/ssl:ro
    depends_on:
      - health-ai-api
    restart: unless-stopped

volumes:
  ollama_data:
    driver: local
```

### 3. Build and Deploy

#### Build Images
```bash
# Build application image
docker build -t healthai-api ./health-ai-api

# Start services
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f health-ai-api
```

#### Create Model in Container
```bash
# Access Ollama container
docker exec -it healthai-ollama bash

# Pull base model
ollama pull llama3.2:latest

# Create health model (copy files first)
docker cp health-dataset.json healthai-ollama:/tmp/
docker cp create_health_model.py healthai-ollama:/tmp/
docker exec -it healthai-ollama python3 /tmp/create_health_model.py
```

## Cloud Deployment

### 1. AWS Deployment

#### EC2 Instance Setup
```bash
# Launch EC2 instance (t3.large or larger recommended)
# Connect via SSH

# Update system
sudo yum update -y

# Install Docker
sudo yum install -y docker
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -a -G docker ec2-user

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

#### Application Load Balancer
```yaml
# cloudformation-template.yaml
AWSTemplateFormatVersion: '2010-09-09'
Resources:
  HealthAILoadBalancer:
    Type: AWS::ElasticLoadBalancingV2::LoadBalancer
    Properties:
      Scheme: internet-facing
      Type: application
      Subnets:
        - subnet-12345678
        - subnet-87654321
      SecurityGroups:
        - !Ref LoadBalancerSecurityGroup

  HealthAITargetGroup:
    Type: AWS::ElasticLoadBalancingV2::TargetGroup
    Properties:
      Port: 8080
      Protocol: HTTP
      VpcId: vpc-12345678
      HealthCheckPath: /api/v1/health
      HealthCheckIntervalSeconds: 30
      HealthCheckTimeoutSeconds: 10
      HealthyThresholdCount: 2
      UnhealthyThresholdCount: 3
```

### 2. Google Cloud Platform

#### Kubernetes Deployment
```yaml
# k8s-deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: healthai-api
spec:
  replicas: 3
  selector:
    matchLabels:
      app: healthai-api
  template:
    metadata:
      labels:
        app: healthai-api
    spec:
      containers:
      - name: healthai-api
        image: gcr.io/your-project/healthai-api:latest
        ports:
        - containerPort: 8080
        env:
        - name: OLLAMA_BASE_URL
          value: "http://ollama-service:11434"
        resources:
          requests:
            memory: "1Gi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "1000m"
        readinessProbe:
          httpGet:
            path: /api/v1/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
---
apiVersion: v1
kind: Service
metadata:
  name: healthai-service
spec:
  selector:
    app: healthai-api
  ports:
  - port: 80
    targetPort: 8080
  type: LoadBalancer
```

### 3. Azure Deployment

#### Container Instances
```bash
# Create resource group
az group create --name healthai-rg --location eastus

# Create container instance
az container create \
  --resource-group healthai-rg \
  --name healthai-api \
  --image your-registry/healthai-api:latest \
  --dns-name-label healthai-unique \
  --ports 8080 \
  --memory 4 \
  --cpu 2 \
  --environment-variables \
    OLLAMA_BASE_URL=http://ollama:11434 \
    SPRING_PROFILES_ACTIVE=azure
```

## Monitoring and Maintenance

### 1. Application Monitoring

#### Prometheus Configuration
```yaml
# prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'healthai-api'
    static_configs:
      - targets: ['localhost:8080']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 10s
```

#### Grafana Dashboard
```json
{
  "dashboard": {
    "title": "Health AI Assistant Metrics",
    "panels": [
      {
        "title": "Response Time",
        "targets": [
          {
            "expr": "histogram_quantile(0.95, sum(rate(http_server_requests_seconds_bucket[5m])) by (le))"
          }
        ]
      },
      {
        "title": "Request Rate",
        "targets": [
          {
            "expr": "rate(http_server_requests_total[5m])"
          }
        ]
      }
    ]
  }
}
```

### 2. Health Checks

#### Application Health Check Script
```bash
#!/bin/bash
# health-check.sh

HEALTH_URL="http://localhost:8080/api/v1/health"
TIMEOUT=10

check_health() {
    response=$(curl -s -o /dev/null -w "%{http_code}" --max-time $TIMEOUT $HEALTH_URL)
    
    if [ "$response" = "200" ]; then
        echo "$(date): Health check PASSED"
        return 0
    else
        echo "$(date): Health check FAILED - HTTP $response"
        return 1
    fi
}

# Run health check
if check_health; then
    exit 0
else
    # Restart service if health check fails
    sudo systemctl restart healthai
    sleep 30
    
    # Check again
    if check_health; then
        echo "$(date): Service restarted successfully"
        exit 0
    else
        echo "$(date): Service restart failed"
        exit 1
    fi
fi
```

#### Cron Job Setup
```bash
# Add to crontab
crontab -e

# Add health check every 5 minutes
*/5 * * * * /opt/healthai/health-check.sh >> /var/log/healthai/health-check.log 2>&1
```

### 3. Log Management

#### Logrotate Configuration
```bash
# /etc/logrotate.d/healthai
/var/log/healthai/*.log {
    daily
    missingok
    rotate 30
    compress
    delaycompress
    notifempty
    create 644 healthai healthai
    postrotate
        systemctl reload healthai
    endscript
}
```

## Troubleshooting

### Common Issues and Solutions

#### 1. Application Won't Start
**Symptoms**: Service fails to start, port binding errors
**Solutions**:
```bash
# Check if port is in use
netstat -tulpn | grep :8080

# Check Java installation
java -version

# Check application logs
sudo journalctl -u healthai -f

# Verify configuration
cat /etc/healthai/application.properties
```

#### 2. Ollama Connection Issues
**Symptoms**: "Ollama is not available" error
**Solutions**:
```bash
# Check Ollama service
sudo systemctl status ollama

# Test Ollama directly
curl http://localhost:11434/api/tags

# Restart Ollama
sudo systemctl restart ollama

# Check model availability
ollama list | grep health-ai-doctor
```

#### 3. Model Not Found
**Symptoms**: Model "health-ai-doctor" not found
**Solutions**:
```bash
# Recreate model
cd /opt/healthai
python3 create_health_model.py

# Verify model creation
ollama list

# Test model
ollama run health-ai-doctor "test question"
```

#### 4. High Memory Usage
**Symptoms**: System running out of memory
**Solutions**:
```bash
# Check memory usage
free -h
ps aux --sort=-%mem | head

# Adjust JVM heap size
# Edit /etc/systemd/system/healthai.service
Environment="JAVA_OPTS=-Xmx1g -Xms512m -XX:+UseG1GC"

# Restart service
sudo systemctl daemon-reload
sudo systemctl restart healthai
```

#### 5. Slow Response Times
**Symptoms**: API responses taking too long
**Solutions**:
```bash
# Check CPU usage
top

# Monitor application metrics
curl http://localhost:8080/actuator/metrics/http.server.requests

# Optimize model parameters
# Edit Modelfile-health-ai to reduce num_predict
```

### Debug Mode

#### Enable Debug Logging
```properties
# Add to application.properties
logging.level.com.healthai=DEBUG
logging.level.reactor.netty=DEBUG
spring.webflux.base-path=/debug
```

#### Performance Profiling
```bash
# Enable JVM profiling
java -XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=healthai-profile.jfr -jar app.jar

# Analyze with JDK Mission Control
jmc healthai-profile.jfr
```

## Security Hardening

### 1. Network Security

#### Firewall Configuration
```bash
# Configure UFW firewall
sudo ufw enable
sudo ufw default deny incoming
sudo ufw default allow outgoing

# Allow SSH
sudo ufw allow ssh

# Allow HTTP/HTTPS
sudo ufw allow 80
sudo ufw allow 443

# Allow application port (internal only)
sudo ufw allow from 127.0.0.1 to any port 8080
```

#### SSL/TLS Configuration
```nginx
# nginx SSL configuration
ssl_protocols TLSv1.2 TLSv1.3;
ssl_ciphers ECDHE-RSA-AES256-GCM-SHA512:DHE-RSA-AES256-GCM-SHA512:ECDHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES256-GCM-SHA384;
ssl_prefer_server_ciphers off;
ssl_session_cache shared:SSL:10m;
ssl_session_timeout 10m;

# HSTS header
add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;
```

### 2. Application Security

#### CORS Configuration
```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("https://your-domain.com")
                .allowedMethods("GET", "POST")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

#### Input Validation
```java
@PostMapping("/chat")
public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
    // Validate message length
    if (request.getMessage().length() > 1000) {
        return ResponseEntity.badRequest()
                .body(ChatResponse.error("Message too long"));
    }
    
    // Sanitize input
    String sanitizedMessage = sanitizeInput(request.getMessage());
    
    // Process request
    return processHealthQuery(sanitizedMessage);
}
```

### 3. Rate Limiting

#### Nginx Rate Limiting
```nginx
# Define rate limiting zones
http {
    limit_req_zone $binary_remote_addr zone=api:10m rate=10r/m;
    limit_req_zone $binary_remote_addr zone=chat:10m rate=5r/m;
}

server {
    # Apply rate limits
    location /api/v1/chat {
        limit_req zone=chat burst=5 nodelay;
        proxy_pass http://localhost:8080;
    }
    
    location /api/ {
        limit_req zone=api burst=10 nodelay;
        proxy_pass http://localhost:8080;
    }
}
```

## Backup and Recovery

### 1. Backup Strategy

#### Application Backup Script
```bash
#!/bin/bash
# backup.sh

BACKUP_DIR="/opt/backups/healthai"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="healthai_backup_$DATE.tar.gz"

# Create backup directory
mkdir -p $BACKUP_DIR

# Backup application files
tar -czf "$BACKUP_DIR/$BACKUP_FILE" \
    /opt/healthai/ \
    /etc/healthai/ \
    /var/log/healthai/ \
    /etc/systemd/system/healthai.service

# Backup Ollama models
ollama list | grep health-ai-doctor && \
    cp ~/.ollama/models/* "$BACKUP_DIR/ollama_models_$DATE/"

# Cleanup old backups (keep 30 days)
find $BACKUP_DIR -name "healthai_backup_*.tar.gz" -mtime +30 -delete

echo "Backup completed: $BACKUP_FILE"
```

#### Automated Backup Schedule
```bash
# Add to crontab
crontab -e

# Daily backup at 2 AM
0 2 * * * /opt/healthai/scripts/backup.sh
```

### 2. Recovery Procedures

#### Application Recovery
```bash
#!/bin/bash
# restore.sh

BACKUP_FILE=$1

if [ -z "$BACKUP_FILE" ]; then
    echo "Usage: $0 <backup_file>"
    exit 1
fi

# Stop services
sudo systemctl stop healthai
sudo systemctl stop ollama

# Restore files
tar -xzf "$BACKUP_FILE" -C /

# Restore permissions
sudo chown -R healthai:healthai /opt/healthai
sudo chown -R healthai:healthai /var/log/healthai

# Reload systemd and start services
sudo systemctl daemon-reload
sudo systemctl start ollama
sudo systemctl start healthai

# Verify recovery
sleep 30
curl -f http://localhost:8080/api/v1/health

echo "Recovery completed successfully"
```

### 3. Disaster Recovery

#### Infrastructure as Code
```terraform
# terraform/main.tf
provider "aws" {
  region = "us-east-1"
}

resource "aws_instance" "healthai" {
  ami           = "ami-0c55b159cbfafe1d0"
  instance_type = "t3.large"
  
  user_data = file("install-script.sh")
  
  tags = {
    Name = "HealthAI-Production"
  }
}

resource "aws_security_group" "healthai_sg" {
  name = "healthai-security-group"
  
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  
  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
```

---

## Quick Reference

### Essential Commands
```bash
# Check application status
sudo systemctl status healthai

# View real-time logs
sudo journalctl -u healthai -f

# Test API health
curl http://localhost:8080/api/v1/health

# Restart services
sudo systemctl restart healthai ollama

# Check Ollama models
ollama list

# Update health model
python3 create_health_model.py
```

### Configuration Files
- Application: `/etc/healthai/application.properties`
- Service: `/etc/systemd/system/healthai.service`
- Nginx: `/etc/nginx/sites-available/healthai`
- Logs: `/var/log/healthai/`

### Important URLs
- Web Interface: `http://localhost:8080`
- Health Check: `http://localhost:8080/api/v1/health`
- API Info: `http://localhost:8080/api/v1/info`
- Chat API: `POST http://localhost:8080/api/v1/chat`

This deployment guide provides comprehensive instructions for setting up the Health AI Assistant in various environments. Choose the deployment method that best fits your infrastructure and requirements.
