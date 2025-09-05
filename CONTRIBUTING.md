# Contributing to Health AI Assistant

Thank you for your interest in contributing to the Health AI Assistant project! This document provides guidelines and information for contributors.

## Table of Contents
- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [Contributing Guidelines](#contributing-guidelines)
- [Pull Request Process](#pull-request-process)
- [Issue Reporting](#issue-reporting)
- [Development Standards](#development-standards)

## Code of Conduct

This project adheres to a code of conduct that we expect all contributors to follow. Please be respectful and constructive in all interactions.

### Our Standards
- Use welcoming and inclusive language
- Be respectful of differing viewpoints and experiences
- Gracefully accept constructive criticism
- Focus on what is best for the community
- Show empathy towards other community members

## Getting Started

### Prerequisites
- Java 21 (OpenJDK or Oracle JDK)
- Maven 3.9.9+
- Ollama runtime
- Git for version control
- Basic understanding of Spring Boot and AI/ML concepts

### Fork and Clone
1. Fork the repository on GitHub
2. Clone your fork locally:
```bash
git clone https://github.com/YOUR-USERNAME/health-ai-assistant.git
cd health-ai-assistant
```

## Development Setup

### 1. Environment Setup
```bash
# Install dependencies (Windows with Chocolatey)
choco install openjdk21 maven

# Install Ollama
winget install Ollama.Ollama

# Verify installations
java -version
mvn -version
ollama --version
```

### 2. Create Development Model
```bash
# Create the health AI model
python create_health_model.py

# Verify model creation
ollama list | grep health-ai-doctor
```

### 3. Run Application
```bash
# Navigate to Spring Boot app
cd health-ai-api

# Build and run in development mode
mvn clean spring-boot:run

# Test the application
curl http://localhost:8080/api/v1/health
```

## Contributing Guidelines

### Areas for Contribution

#### 🧠 **AI Model Improvements**
- Adding new health topics to `health-dataset.json`
- Improving model responses and accuracy
- Optimizing model parameters
- Adding specialized medical domains

#### 🎨 **Frontend Enhancements**
- UI/UX improvements
- Accessibility features
- Mobile responsiveness
- New interactive features

#### ⚙️ **Backend Development**
- API endpoint enhancements
- Performance optimizations
- Security improvements
- Integration features

#### 📚 **Documentation**
- Code documentation improvements
- User guides and tutorials
- API documentation updates
- Deployment guides

#### 🧪 **Testing**
- Unit test coverage
- Integration tests
- Performance testing
- Security testing

### Types of Contributions Welcome
- 🐛 **Bug fixes**
- ✨ **New features**
- 📝 **Documentation improvements**
- 🎨 **UI/UX enhancements**
- ⚡ **Performance optimizations**
- 🔒 **Security improvements**
- 🌐 **Internationalization**

## Pull Request Process

### 1. Create a Feature Branch
```bash
git checkout -b feature/your-feature-name
# or
git checkout -b bugfix/issue-description
```

### 2. Make Your Changes
- Follow the coding standards outlined below
- Add tests for new functionality
- Update documentation as needed
- Ensure all tests pass

### 3. Commit Your Changes
Use conventional commit messages:
```bash
git commit -m "feat: add new health topic for diabetes management"
git commit -m "fix: resolve session timeout issue"
git commit -m "docs: update API documentation"
```

### 4. Push and Create Pull Request
```bash
git push origin feature/your-feature-name
```

Then create a pull request on GitHub with:
- Clear description of changes
- Link to related issues
- Screenshots for UI changes
- Test results

### 5. Code Review Process
- Maintainers will review your PR
- Address any feedback or requested changes
- Once approved, your PR will be merged

## Issue Reporting

### Before Creating an Issue
1. Check existing issues for duplicates
2. Review documentation for solutions
3. Test with the latest version

### Creating a Good Issue
Include:
- **Clear title** describing the problem
- **Detailed description** of the issue
- **Steps to reproduce** the problem
- **Expected vs actual behavior**
- **Environment information** (OS, Java version, etc.)
- **Error logs** if applicable
- **Screenshots** for UI issues

### Issue Templates

#### Bug Report
```markdown
**Describe the bug**
A clear description of what the bug is.

**To Reproduce**
Steps to reproduce the behavior:
1. Go to '...'
2. Click on '....'
3. See error

**Expected behavior**
What you expected to happen.

**Environment:**
- OS: [e.g. Windows 11]
- Java Version: [e.g. OpenJDK 21]
- Maven Version: [e.g. 3.9.9]
- Ollama Version: [e.g. 0.1.0]
```

#### Feature Request
```markdown
**Is your feature request related to a problem?**
A clear description of what the problem is.

**Describe the solution you'd like**
A clear description of what you want to happen.

**Additional context**
Add any other context about the feature request.
```

## Development Standards

### Java Code Standards

#### Code Style
- Follow Spring Boot best practices
- Use meaningful variable and method names
- Keep methods focused and small
- Add JavaDoc for public methods

#### Example:
```java
/**
 * Processes health queries and returns AI-generated responses.
 * 
 * @param userMessage The user's health question
 * @param sessionId Unique session identifier
 * @return ChatResponse containing AI advice and metadata
 * @throws HealthChatException if processing fails
 */
public ChatResponse getResponse(String userMessage, String sessionId) {
    // Implementation
}
```

### Testing Standards

#### Unit Tests
```java
@Test
void shouldReturnHealthAdviceForValidQuestion() {
    // Given
    String question = "How can I prevent UTIs?";
    
    // When
    ChatResponse response = healthChatService.getResponse(question, "test-session");
    
    // Then
    assertThat(response.isSuccess()).isTrue();
    assertThat(response.getResponse()).contains("UTI");
    assertThat(response.getResponse()).contains("consult a healthcare professional");
}
```

#### Integration Tests
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerIntegrationTest {
    
    @Test
    void shouldReturnHealthAdviceViaAPI() {
        // Test API endpoint integration
    }
}
```

### Frontend Standards

#### JavaScript
- Use modern ES6+ features
- Follow consistent naming conventions
- Add comments for complex logic
- Ensure cross-browser compatibility

#### CSS
- Use semantic class names
- Follow BEM methodology when appropriate
- Ensure responsive design
- Maintain accessibility standards

### Documentation Standards

#### Code Documentation
- Use clear, concise comments
- Document complex algorithms
- Include usage examples
- Keep documentation up-to-date

#### API Documentation
- Follow OpenAPI 3.0 standards
- Include request/response examples
- Document error codes and messages
- Provide SDK examples

### Health Data Standards

When adding new health topics:

#### Training Data Format
```json
{
  "question": "Clear, specific health question",
  "answer": "Comprehensive answer with medical disclaimer: 'This is general information; consult a healthcare professional for personalized advice.'"
}
```

#### Medical Safety Requirements
- Always include medical disclaimers
- Avoid diagnostic language
- Recommend professional consultation
- Use evidence-based information
- Cite reliable sources when possible

### Git Workflow

#### Commit Message Format
```
<type>(<scope>): <subject>

<body>

<footer>
```

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes
- `refactor`: Code refactoring
- `test`: Adding tests
- `chore`: Maintenance tasks

#### Branch Naming
- `feature/description-of-feature`
- `bugfix/description-of-bug`
- `hotfix/critical-issue`
- `docs/documentation-update`

## Performance Guidelines

### Backend Performance
- Use reactive programming patterns
- Implement proper caching strategies
- Monitor response times
- Optimize database queries (when applicable)

### Frontend Performance
- Minimize HTTP requests
- Optimize images and assets
- Use efficient CSS selectors
- Implement proper loading states

## Security Guidelines

### Input Validation
- Validate all user inputs
- Sanitize data before processing
- Implement rate limiting
- Use parameterized queries

### API Security
- Implement proper authentication
- Use HTTPS in production
- Validate request headers
- Handle errors securely

## Release Process

### Version Numbering
Follow Semantic Versioning (SemVer):
- `MAJOR.MINOR.PATCH`
- Major: Breaking changes
- Minor: New features (backward compatible)
- Patch: Bug fixes

### Release Checklist
- [ ] All tests passing
- [ ] Documentation updated
- [ ] Performance benchmarks run
- [ ] Security review completed
- [ ] Deployment tested

## Getting Help

### Community Support
- GitHub Discussions for questions
- Issues for bug reports
- Pull requests for contributions

### Development Questions
- Check existing documentation first
- Search closed issues for solutions
- Create a discussion for complex questions

## Recognition

Contributors will be recognized in:
- README.md contributors section
- Release notes
- Project documentation

Thank you for contributing to the Health AI Assistant project! 🙏

---

*Remember: This project deals with health information. Always prioritize user safety and include appropriate medical disclaimers in any health-related content.*
