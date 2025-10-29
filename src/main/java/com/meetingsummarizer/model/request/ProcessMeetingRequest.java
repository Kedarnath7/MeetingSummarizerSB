package com.meetingsummarizer.model.request;

import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotNull;

public class ProcessMeetingRequest {

    @NotNull(message = "Audio file is required")
    private MultipartFile audioFile;

    private String meetingTitle;

    // Getters and Setters
    public MultipartFile getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(MultipartFile audioFile) {
        this.audioFile = audioFile;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }
}