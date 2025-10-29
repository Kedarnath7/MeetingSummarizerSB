package com.meetingsummarizer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.HashMap;

@Service
public class TranscriptionService {

    private final WebClient webClient;

    @Value("${app.assemblyai.api-key}")
    private String apiKey;

    public TranscriptionService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.assemblyai.com/v2")
                .defaultHeader(HttpHeaders.AUTHORIZATION, apiKey)
                .build();
    }

    public String transcribeAudio(MultipartFile audioFile) {
        try {
            // Step 1: Upload audio file and get upload URL
            String uploadUrl = uploadAudioFile(audioFile);

            // Step 2: Start transcription
            String transcriptId = startTranscription(uploadUrl);

            // Step 3: Poll for results
            return pollTranscriptionResult(transcriptId);

        } catch (Exception e) {
            throw new RuntimeException("Transcription failed: " + e.getMessage(), e);
        }
    }

    private String uploadAudioFile(MultipartFile audioFile) {
        // In a real implementation, you'd upload to AssemblyAI's upload endpoint
        // For now, we'll simulate this step
        return "https://example.com/uploaded-audio/" + audioFile.getOriginalFilename();
    }

    private String startTranscription(String audioUrl) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("audio_url", audioUrl);

        Map<String, Object> response = webClient.post()
                .uri("/transcript")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return (String) response.get("id");
    }

    private String pollTranscriptionResult(String transcriptId) {
        int maxAttempts = 30;
        int attempt = 0;

        while (attempt < maxAttempts) {
            Map<String, Object> response = webClient.get()
                    .uri("/transcript/" + transcriptId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            String status = (String) response.get("status");

            if ("completed".equals(status)) {
                return (String) response.get("text");
            } else if ("failed".equals(status)) {
                throw new RuntimeException("Transcription failed: " + response.get("error"));
            }

            try {
                Thread.sleep(2000); // Wait 2 seconds before polling again
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Transcription polling interrupted", e);
            }
            attempt++;
        }

        throw new RuntimeException("Transcription timeout");
    }
}