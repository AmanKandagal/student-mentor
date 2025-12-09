package com.studnetmentor.student_mentor_backend.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfileResponse {
    private Long profileId;
    private Long userId;
    private String name;
    private String university;
    private String branch;
    private Integer graduationYear;
    private Double cgpa;
    private String about;
    private String skills;

    private String linkedinUrl;
    private String resumeUrl;
}