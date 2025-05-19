# External API Spring Boot Application

A Spring Boot application that provides RESTful API endpoints for text generation and audio conversion services.

## Project Overview

This application is built using Spring Boot 3.2.3 and provides endpoints for:
- Text generation based on input content
- Audio generation from text
- Basic API testing functionality

## Prerequisites

- Java 17 or higher
- Maven
- Spring Boot 3.2.3
- Gemini API Key
- ElevenLabs API Key

## API Configuration

Add the following configurations to your `src/main/resources/application.properties` file:

```properties
# Gemini API Configuration
gemini.api.key=your_gemini_api_key_here

# ElevenLabs API Configuration
elevenlabs.api.key=your_elevenlabs_api_key_here
```

### Getting API Keys
1. **Gemini API Key**:
   - Visit [Google AI Studio](https://makersuite.google.com/app/apikey)
   - Create a new API key
   - Copy and paste it into the configuration

2. **ElevenLabs API Key**:
   - Visit [ElevenLabs Dashboard](https://elevenlabs.io/app)
   - Navigate to your profile settings
   - Copy your API key
   - Paste it into the configuration

## Project Structure

```
src/main/java/com/example/externalapispringboot/
├── config/         # Configuration classes
├── controller/     # REST API controllers
├── request/        # Request DTOs
├── response/       # Response DTOs
└── service/        # Business logic services
```

## API Endpoints

### 1. Search and Generate Audio
- **Endpoint**: `POST /api/search`
- **Content-Type**: `application/json`
- **Description**: Generates text based on input content and converts it to audio
- **Request Body**: JSON object containing content
- **Response**: Audio file (MP3)

### 2. Generate Audio from Text
- **Endpoint**: `POST /api/generate-audio`
- **Content-Type**: `application/json`
- **Description**: Converts provided text to audio
- **Request Body**: JSON object with "text" field
- **Response**: Audio file (MP3)

### 3. Test Endpoint
- **Endpoint**: `GET /api/test`
- **Description**: Simple endpoint to verify API functionality
- **Response**: "API is working!"

## Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Test
- Lombok
- Other dependencies as specified in `pom.xml`

## Building and Running

1. Clone the repository
2. Navigate to the project directory
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Error Handling

The application includes comprehensive error handling for:
- Invalid input
- Service configuration errors
- General processing errors

All errors are logged and returned with appropriate HTTP status codes.

## Cross-Origin Resource Sharing (CORS)

The API is configured to accept requests from any origin (`@CrossOrigin(origins = "*")`).

## Logging

The application uses SLF4J for logging, with detailed logging for:
- API requests
- Error conditions
- Successful operations

