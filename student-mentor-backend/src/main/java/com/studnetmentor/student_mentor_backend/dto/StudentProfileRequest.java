package com.studnetmentor.student_mentor_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentProfileRequest {

    @NotNull
    private Integer graduationYear;

    @NotBlank
    private String university;

    @NotBlank
    private String branch;

    @NotNull
    private Double cgpa;

    private String about;

    @NotBlank
    private String skills;

    private String linkedinUrl;
    private String resumeUrl;
}