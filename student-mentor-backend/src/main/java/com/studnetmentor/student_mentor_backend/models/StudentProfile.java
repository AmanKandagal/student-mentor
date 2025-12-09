package com.studnetmentor.student_mentor_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private int graduationYear;

    private String university;

    private String branch;

    private double cgpa;

    @Column(length = 1000)
    private String about;

    private String skills; 

    private String linkedinUrl;
    private String resumeUrl;
}