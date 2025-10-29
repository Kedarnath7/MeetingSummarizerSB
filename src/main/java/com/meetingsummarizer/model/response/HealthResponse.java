package com.meetingsummarizer.model.response;

import java.time.LocalDateTime;
import java.util.Map;

public class HealthResponse {
    private String status;
    private LocalDateTime timestamp;
    private Map<String, String> services;

    public HealthResponse(String status, Map<String, String> services) {
        this.status = status;
        this.services = services;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Map<String, String> getServices() { return services; }
    public void setServices(Map<String, String> services) { this.services = services; }
}