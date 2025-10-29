package com.meetingsummarizer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class TranscriptionService {

    private final RestTemplate restTemplate;

    @Value("${app.assemblyai.api-key:test-key}")
    private String apiKey;

    public TranscriptionService() {
        this.restTemplate = new RestTemplate();
    }

    public String transcribeAudio(MultipartFile audioFile) {
        try {
            // For demo purposes, return mock transcript
            // In real implementation, integrate with AssemblyAI API

            String mockTranscript = """
                This is a mock transcript of a team meeting.
                We discussed the Q2 project timelines and resource allocation.
                John mentioned that the frontend development is on track.
                Sarah reported that the backend API will be ready by next week.
                The team decided to hire two additional developers.
                Action items: John will prepare the budget proposal by Friday.
                Sarah will schedule technical interviews for the new positions.
                """;

            // Simulate processing time
            Thread.sleep(2000);

            return mockTranscript;

        } catch (Exception e) {
            throw new RuntimeException("Transcription failed: " + e.getMessage(), e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", apiKey);
        headers.set("Content-Type", "application/json");
        return headers;
    }
}