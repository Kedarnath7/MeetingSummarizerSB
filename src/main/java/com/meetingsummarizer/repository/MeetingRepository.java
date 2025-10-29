package com.meetingsummarizer.repository;

import com.meetingsummarizer.model.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Optional<Meeting> findByMeetingId(String meetingId);
    List<Meeting> findAllByOrderByProcessedAtDesc();
}