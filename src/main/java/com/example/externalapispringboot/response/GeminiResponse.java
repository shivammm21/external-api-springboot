package com.example.externalapispringboot.response;

import lombok.Data;

import java.util.List;

@Data
public class GeminiResponse {
    private List<Candidate> candidates;
    private String modelVersion;

} 