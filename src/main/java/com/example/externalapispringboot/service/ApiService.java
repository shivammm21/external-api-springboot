package com.example.externalapispringboot.service;

import com.example.externalapispringboot.request.Content;
import com.example.externalapispringboot.response.GeminiResponse;
import com.example.externalapispringboot.response.Candidate;
import com.example.externalapispringboot.response.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.Base64;

@Service
public class ApiService {
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    private final RestTemplate restTemplate;

    @Value("${google.api.key}")
    private String googleApiKey;

    @Value("${elevenlabs.api.key}")
    private String elevenLabsApiKey;

    @Value("${elevenlabs.voice.id}")
    private String elevenLabsVoiceId;

    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private static final String ELEVENLABS_URL = "https://api.elevenlabs.io/v1/text-to-speech/";

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateText(Content content) {
        try {
            if (googleApiKey == null || googleApiKey.trim().isEmpty()) {
                throw new IllegalStateException("Google API key is not configured");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = Map.of(
                "contents", content.getContents().stream()
                    .map(item -> Map.of(
                        "parts", item.getParts().stream()
                            .map(part -> Map.of("text", part.getText()))
                            .toList()
                    ))
                    .toList()
            );

            logger.debug("Sending request to Gemini API with body: {}", requestBody);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            String url = GEMINI_URL + "?key=" + googleApiKey;

            ResponseEntity<GeminiResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                GeminiResponse.class
            );

            GeminiResponse body = response.getBody();
            if (body != null && body.getCandidates() != null && !body.getCandidates().isEmpty()) {
                String generatedText = body.getCandidates().get(0).getContent().getParts().get(0).getText();
                logger.debug("Received response from Gemini API: {}", generatedText);
                return generatedText;
            }
            logger.warn("No response content from Gemini API");
            return "No response from Gemini API";
        } catch (Exception e) {
            logger.error("Error calling Gemini API: {}", e.getMessage(), e);
            throw new RuntimeException("Error calling Gemini API: " + e.getMessage(), e);
        }
    }

    public byte[] generateAudio(String text) {
        try {
            if (elevenLabsApiKey == null || elevenLabsApiKey.trim().isEmpty()) {
                throw new IllegalStateException("ElevenLabs API key is not configured");
            }

            if (elevenLabsVoiceId == null || elevenLabsVoiceId.trim().isEmpty()) {
                throw new IllegalStateException("ElevenLabs voice ID is not configured");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("xi-api-key", elevenLabsApiKey);

            Map<String, Object> requestBody = Map.of(
                "text", text,
                "model_id", "eleven_monolingual_v1",
                "voice_settings", Map.of(
                    "stability", 0.5,
                    "similarity_boost", 0.75
                )
            );

            logger.debug("Sending request to ElevenLabs API for text: {}", text);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            String url = ELEVENLABS_URL + elevenLabsVoiceId;

            ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                byte[].class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                logger.debug("Successfully generated audio from ElevenLabs API");
                return response.getBody();
            }

            logger.warn("No audio content received from ElevenLabs API");
            throw new RuntimeException("Failed to generate audio from ElevenLabs API");
        } catch (Exception e) {
            logger.error("Error calling ElevenLabs API: {}", e.getMessage(), e);
            throw new RuntimeException("Error calling ElevenLabs API: " + e.getMessage(), e);
        }
    }
}
