package com.meetingsummarizer.model.response;

import java.time.LocalDateTime;
import java.util.List;

public class MeetingSummaryResponse {
    private String meetingId;
    private String title;
    private String summary;
    private List<String> keyDecisions;
    private List<ActionItem> actionItems;
    private String transcript;
    private LocalDateTime processedAt;
    private Double confidenceScore;

    // Constructors
    public MeetingSummaryResponse() {}

    public MeetingSummaryResponse(String meetingId, String title, String summary,
                                  List<String> keyDecisions, List<ActionItem> actionItems,
                                  String transcript, LocalDateTime processedAt, Double confidenceScore) {
        this.meetingId = meetingId;
        this.title = title;
        this.summary = summary;
        this.keyDecisions = keyDecisions;
        this.actionItems = actionItems;
        this.transcript = transcript;
        this.processedAt = processedAt;
        this.confidenceScore = confidenceScore;
    }

    // Getters and Setters
    public String getMeetingId() { return meetingId; }
    public void setMeetingId(String meetingId) { this.meetingId = meetingId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public List<String> getKeyDecisions() { return keyDecisions; }
    public void setKeyDecisions(List<String> keyDecisions) { this.keyDecisions = keyDecisions; }

    public List<ActionItem> getActionItems() { return actionItems; }
    public void setActionItems(List<ActionItem> actionItems) { this.actionItems = actionItems; }

    public String getTranscript() { return transcript; }
    public void setTranscript(String transcript) { this.transcript = transcript; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }

    public static class ActionItem {
        private String task;
        private String assignee;
        private String deadline;

        public ActionItem() {}

        public ActionItem(String task, String assignee, String deadline) {
            this.task = task;
            this.assignee = assignee;
            this.deadline = deadline;
        }

        // Getters and Setters
        public String getTask() { return task; }
        public void setTask(String task) { this.task = task; }

        public String getAssignee() { return assignee; }
        public void setAssignee(String assignee) { this.assignee = assignee; }

        public String getDeadline() { return deadline; }
        public void setDeadline(String deadline) { this.deadline = deadline; }
    }
}