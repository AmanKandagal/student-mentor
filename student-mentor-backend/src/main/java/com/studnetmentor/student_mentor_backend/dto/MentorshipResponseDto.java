package com.studnetmentor.student_mentor_backend.dto;

import com.studnetmentor.student_mentor_backend.models.RequestStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class MentorshipResponseDto {
    private Long requestId;
    private String message;
    private RequestStatus status;
    private LocalDateTime createdAt;
    
    private Long studentId;
    private String studentName; 
    private String studentEmail;
    
    private String studentResumeUrl;
    private String studentLinkedinUrl;

    private Long mentorId;
    private String mentorName;
}