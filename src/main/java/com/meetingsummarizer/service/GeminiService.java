package com.meetingsummarizer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GeminiService {

    private final RestTemplate restTemplate;

    @Value("${app.gemini.api-key:test-key}")
    private String apiKey;

    public GeminiService() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> generateSummary(String transcript) {
        try {
            // Mock response for demo
            // In real implementation, call Google Gemini API

            Map<String, Object> result = new HashMap<>();
            result.put("summary", "The team meeting focused on Q2 project timelines and resource allocation. Frontend development is progressing well, while backend APIs are scheduled for completion next week. Key decisions were made regarding team expansion.");

            result.put("keyDecisions", Arrays.asList(
                    "Approve hiring of two additional developers",
                    "Extend project deadline by one week",
                    "Allocate additional budget for marketing campaign"
            ));

            List<Map<String, String>> actionItems = new ArrayList<>();
            actionItems.add(createActionItem("Prepare budget proposal", "John Doe", "2024-02-15"));
            actionItems.add(createActionItem("Schedule technical interviews", "Sarah Smith", "2024-02-20"));
            actionItems.add(createActionItem("Update project timeline", "Mike Johnson", "2024-02-18"));

            result.put("actionItems", actionItems);

            // Simulate processing time
            Thread.sleep(1000);

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Summary generation failed: " + e.getMessage(), e);
        }
    }

    private Map<String, String> createActionItem(String task, String assignee, String deadline) {
        Map<String, String> actionItem = new HashMap<>();
        actionItem.put("task", task);
        actionItem.put("assignee", assignee);
        actionItem.put("deadline", deadline);
        return actionItem;
    }
}