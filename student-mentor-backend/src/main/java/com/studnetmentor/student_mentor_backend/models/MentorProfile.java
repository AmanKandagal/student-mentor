package com.studnetmentor.student_mentor_backend.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "mentor_profiles")
public class MentorProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String company;
    private String jobTitle;
    private String skills; 
    
    @Column(length = 1000)
    private String bio;
    
    private Integer yearsOfExperience;

    private String languages; 
    private String linkedinUrl;
}