package com.ResumeForge.ResumeForgeAI.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
@Service
public class GeminiService {
	@Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String baseUrl;

    @Value("${gemini.api.model}")
    private String model;

    private final RestTemplate restTemplate;

    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public String generateContent(String prompt) {
        String url = String.format("%s/%s:generateContent?key=%s", baseUrl, model, apiKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(
            Map.of("parts", List.of(
                Map.of("text", prompt)
            ))
        ));

        // Generation configuration
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.7);
        generationConfig.put("maxOutputTokens", 2000);
        generationConfig.put("topP", 0.8);
        requestBody.put("generationConfig", generationConfig);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            return extractGeneratedText(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Gemini API call failed: " + e.getMessage());
        }
    }

    private String extractGeneratedText(Map<String, Object> response) {
        if (response == null) throw new RuntimeException("Empty response from Gemini API");

        if (response.containsKey("error")) {
            Map<String, Object> error = (Map<String, Object>) response.get("error");
            throw new RuntimeException("Gemini API error: " + error.get("message"));
        }

        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
        if (candidates == null || candidates.isEmpty()) {
            throw new RuntimeException("No candidates generated");
        }

        Map<String, Object> firstCandidate = candidates.get(0);
        Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
        
        String text = (String) parts.get(0).get("text");
        return text != null ? text.trim() : "";
    }
}
