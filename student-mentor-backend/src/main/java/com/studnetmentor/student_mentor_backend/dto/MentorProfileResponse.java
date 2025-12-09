package com.studnetmentor.student_mentor_backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MentorProfileResponse {
    private Long profileId;
    private Long userId;
    private String name;
    private String email;
    private String company;
    private String jobTitle;
    private String skills;
    private String bio;
    private Integer yearsOfExperience;

    // New Fields
    private String languages;
    private String linkedinUrl;
}
