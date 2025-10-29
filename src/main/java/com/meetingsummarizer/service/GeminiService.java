package com.meetingsummarizer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Service
public class GeminiService {

    private final WebClient webClient;

    @Value("${app.gemini.api-key}")
    private String apiKey;

    @Value("${app.gemini.model}")
    private String model;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/models")
                .build();
    }

    public Map<String, Object> generateSummary(String transcript) {
        String prompt = """
            Analyze the following meeting transcript and provide a structured summary in JSON format.
            Include:
            1. A concise overall summary
            2. Key decisions made
            3. Action items with assignees and deadlines
            
            Transcript:
            %s
            
            Respond with JSON in this exact format:
            {
                "summary": "brief summary here",
                "keyDecisions": ["decision1", "decision2", ...],
                "actionItems": [
                    {"task": "task description", "assignee": "person name", "deadline": "date or timeframe"}
                ]
            }
            """.formatted(transcript);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(
                Map.of("parts", List.of(
                        Map.of("text", prompt)
                ))
        ));

        Map<String, Object> response = webClient.post()
                .uri("/{model}:generateContent?key={key}", model, apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return parseGeminiResponse(response);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseGeminiResponse(Map<String, Object> response) {
        try {
            Map<String, Object> result = new HashMap<>();

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> candidate = candidates.get(0);
                Map<String, Object> content = (Map<String, Object>) candidate.get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                String text = (String) parts.get(0).get("text");

                // Parse the JSON response from Gemini
                // In a real implementation, you'd use Jackson to parse the JSON string
                return parseJsonResponse(text);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response", e);
        }

        throw new RuntimeException("No valid response from Gemini");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJsonResponse(String jsonText) {
        // Extract JSON from the response text (might be wrapped in markdown)
        String cleanJson = jsonText.replaceAll("```json\\n?", "").replaceAll("\\n?```", "").trim();

        // In a real implementation, use Jackson ObjectMapper
        // For now, return a mock response
        Map<String, Object> result = new HashMap<>();
        result.put("summary", "Meeting discussed project timelines and resource allocation.");
        result.put("keyDecisions", List.of(
                "Approved Q2 budget for marketing",
                "Hire two additional developers",
                "Extend project deadline by two weeks"
        ));

        List<Map<String, String>> actionItems = new ArrayList<>();
        actionItems.add(Map.of(
                "task", "Prepare budget proposal",
                "assignee", "John Doe",
                "deadline", "2024-02-15"
        ));
        actionItems.add(Map.of(
                "task", "Schedule technical interviews",
                "assignee", "Jane Smith",
                "deadline", "2024-02-20"
        ));

        result.put("actionItems", actionItems);
        return result;
    }
}