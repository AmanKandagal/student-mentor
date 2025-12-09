package com.studnetmentor.student_mentor_backend.dto;

import lombok.Data;

@Data
public class MentorshipRequestDto {
    private Long studentId;
    private Long mentorId;
    private String message;
    
    
}