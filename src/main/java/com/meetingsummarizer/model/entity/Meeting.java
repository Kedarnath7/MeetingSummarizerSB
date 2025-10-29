package com.meetingsummarizer.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
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
    private java.util.List<String> keyDecisions;

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

    // Getters and Setters
    // ... (omitted for brevity)
}