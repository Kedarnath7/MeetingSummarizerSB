package com.meetingsummarizer.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AIService {

    private final TranscriptionService transcriptionService;
    private final GeminiService geminiService;

    public AIService(TranscriptionService transcriptionService, GeminiService geminiService) {
        this.transcriptionService = transcriptionService;
        this.geminiService = geminiService;
    }

    public Map<String, Object> processMeeting(String audioFilePath) {
        try {
            // Step 1: Transcribe audio
            String transcript = transcriptionService.transcribeAudio(null); // Pass actual file

            // Step 2: Generate summary using Gemini
            Map<String, Object> summary = geminiService.generateSummary(transcript);

            // Add transcript to result
            summary.put("transcript", transcript);
            summary.put("confidenceScore", 0.92); // Mock confidence score

            return summary;

        } catch (Exception e) {
            throw new RuntimeException("AI processing failed: " + e.getMessage(), e);
        }
    }
}