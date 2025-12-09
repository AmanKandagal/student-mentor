package com.studnetmentor.student_mentor_backend.dto;

import lombok.Data;

@Data
public class MentorProfileRequest {
    private String company;
    private String jobTitle;
    private String skills;
    private String bio;
    private Integer yearsOfExperience;
    
    private String languages;
    private String linkedinUrl;
}