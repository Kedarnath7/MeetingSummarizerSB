package com.meetingsummarizer.service;

import com.meetingsummarizer.model.entity.Meeting;
import com.meetingsummarizer.model.response.MeetingSummaryResponse;
import com.meetingsummarizer.repository.MeetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final AIService aiService;

    public MeetingService(MeetingRepository meetingRepository, AIService aiService) {
        this.meetingRepository = meetingRepository;
        this.aiService = aiService;
    }

    public MeetingSummaryResponse processMeeting(MultipartFile audioFile, String title) {
        try {
            // Generate unique meeting ID
            String meetingId = UUID.randomUUID().toString();

            // Process with AI services
            Map<String, Object> aiResult = aiService.processMeeting(null); // Pass actual file path

            // Create meeting entity
            Meeting meeting = new Meeting();
            meeting.setMeetingId(meetingId);
            meeting.setTitle(title != null ? title : "Meeting " + LocalDateTime.now().toString());
            meeting.setTranscript((String) aiResult.get("transcript"));
            meeting.setSummary((String) aiResult.get("summary"));
            meeting.setKeyDecisions((List<String>) aiResult.get("keyDecisions"));

            // Convert action items to map
            @SuppressWarnings("unchecked")
            List<Map<String, String>> actionItems = (List<Map<String, String>>) aiResult.get("actionItems");
            Map<String, String> actionItemsMap = actionItems.stream()
                    .collect(Collectors.toMap(
                            item -> item.get("task"),
                            item -> item.get("assignee") + "|" + item.get("deadline")
                    ));
            meeting.setActionItems(actionItemsMap);

            meeting.setConfidenceScore((Double) aiResult.get("confidenceScore"));
            meeting.setProcessedAt(LocalDateTime.now());

            // Save to database
            Meeting savedMeeting = meetingRepository.save(meeting);

            // Convert to response
            return convertToResponse(savedMeeting);

        } catch (Exception e) {
            throw new RuntimeException("Meeting processing failed: " + e.getMessage(), e);
        }
    }

    public List<MeetingSummaryResponse> getAllMeetings() {
        return meetingRepository.findAllByOrderByProcessedAtDesc().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public MeetingSummaryResponse getMeetingById(String meetingId) {
        Meeting meeting = meetingRepository.findByMeetingId(meetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found: " + meetingId));
        return convertToResponse(meeting);
    }

    private MeetingSummaryResponse convertToResponse(Meeting meeting) {
        List<MeetingSummaryResponse.ActionItem> actionItems = meeting.getActionItems().entrySet().stream()
                .map(entry -> {
                    String[] parts = entry.getValue().split("\\|");
                    return new MeetingSummaryResponse.ActionItem(
                            entry.getKey(),
                            parts.length > 0 ? parts[0] : "",
                            parts.length > 1 ? parts[1] : ""
                    );
                })
                .collect(Collectors.toList());

        return new MeetingSummaryResponse(
                meeting.getMeetingId(),
                meeting.getTitle(),
                meeting.getSummary(),
                meeting.getKeyDecisions(),
                actionItems,
                meeting.getTranscript(),
                meeting.getProcessedAt(),
                meeting.getConfidenceScore()
        );
    }
}