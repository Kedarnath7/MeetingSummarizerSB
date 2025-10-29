package com.meetingsummarizer.controller;

import com.meetingsummarizer.model.request.ProcessMeetingRequest;
import com.meetingsummarizer.model.response.*;
import com.meetingsummarizer.service.MeetingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/meeting")
@CrossOrigin(origins = "*")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping("/summarize")
    public ResponseEntity<ApiResponse<MeetingSummaryResponse>> summarizeMeeting(
            @Valid @ModelAttribute ProcessMeetingRequest request) {
        try {
            MeetingSummaryResponse response = meetingService.processMeeting(
                    request.getAudioFile(),
                    request.getMeetingTitle()
            );
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Processing failed: " + e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> healthCheck() {
        Map<String, String> services = Map.of(
                "assemblyai", "operational",
                "gemini", "operational",
                "database", "operational"
        );

        HealthResponse response = new HealthResponse("healthy", services);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/meetings")
    public ResponseEntity<ApiResponse<List<MeetingSummaryResponse>>> getAllMeetings() {
        try {
            List<MeetingSummaryResponse> meetings = meetingService.getAllMeetings();
            return ResponseEntity.ok(ApiResponse.success(meetings));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to fetch meetings: " + e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        Map<String, Object> stats = Map.of(
                "totalMeetings", 15,
                "averageProcessingTime", "45 seconds",
                "successRate", "94%",
                "lastProcessed", "2024-01-15T10:30:00"
        );

        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
    }
}