package com.example.externalapispringboot.controller;

import com.example.externalapispringboot.request.Content;
import com.example.externalapispringboot.service.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, 
                  consumes = MediaType.APPLICATION_JSON_VALUE, 
                  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(@RequestBody Content content) {
        try {
            logger.info("Received request to /api/search with content: {}", content);
            
            if (content == null) {
                return createErrorResponse("Content object is null", HttpStatus.BAD_REQUEST);
            }
            
            if (content.getContents() == null || content.getContents().isEmpty()) {
                return createErrorResponse("Content list is empty or null", HttpStatus.BAD_REQUEST);
            }

            String response = apiService.generateText(content);

            System.out.println(response);

            byte[] audioData = apiService.generateAudio(response);

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("response", response);
            responseMap.put("content", content);
            
            logger.info("Successfully generated response");
            //return ResponseEntity.ok(responseMap);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"audio.mp3\"")
                    .body(audioData);
            
        } catch (IllegalStateException e) {
            logger.error("Configuration error: {}", e.getMessage());
            return createErrorResponse("Service configuration error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Error processing request: {}", e.getMessage(), e);
            return createErrorResponse("Error processing request: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/generate-audio", 
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<?> generateAudio(@RequestBody Map<String, String> request) {
        try {
            logger.info("Received request to /api/generate-audio");
            
            String text = request.get("text");
            if (text == null || text.trim().isEmpty()) {
                return createErrorResponse("Text is required", HttpStatus.BAD_REQUEST);
            }

            byte[] audioData = apiService.generateAudio(text);
            
            logger.info("Successfully generated audio");
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"audio.mp3\"")
                .body(audioData);
            
        } catch (IllegalStateException e) {
            logger.error("Configuration error: {}", e.getMessage());
            return createErrorResponse("Service configuration error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("Error generating audio: {}", e.getMessage(), e);
            return createErrorResponse("Error generating audio: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Map<String, String>> createErrorResponse(String message, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        return new ResponseEntity<>(errorResponse, status);
    }

//    @GetMapping("/voices")
//    public ResponseEntity<?> getVoices() {
//        try {
//            logger.info("Received request to /api/voices");
//            List<Map<String, Object>> voices = apiService.getAvailableVoices();
//            return ResponseEntity.ok(voices);
//        } catch (Exception e) {
//            logger.error("Error getting voices: {}", e.getMessage());
//            return ResponseEntity.internalServerError()
//                .body("Error: " + e.getMessage());
//        }
//    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        logger.info("Test endpoint called");
        return ResponseEntity.ok("API is working!");
    }
}
