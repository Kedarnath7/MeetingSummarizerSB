package com.meetingsummarizer.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "meetings")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String meetingId;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String transcript;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @ElementCollection
    @CollectionTable(name = "meeting_decisions", joinColumns = @JoinColumn(name = "meeting_id"))
    @Column(name = "decision")
    private List<String> keyDecisions;

    @ElementCollection
    @CollectionTable(name = "meeting_action_items", joinColumns = @JoinColumn(name = "meeting_id"))
    @MapKeyColumn(name = "task")
    @Column(name = "assignee_deadline")
    private Map<String, String> actionItems;

    private Double confidenceScore;
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public Meeting() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTranscript() { return transcript; }
    public void setTranscript(String transcript) { this.transcript = transcript; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public List<String> getKeyDecisions() { return keyDecisions; }
    public void setKeyDecisions(List<String> keyDecisions) { this.keyDecisions = keyDecisions; }

    public Map<String, String> getActionItems() { return actionItems; }
    public void setActionItems(Map<String, String> actionItems) { this.actionItems = actionItems; }

    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}