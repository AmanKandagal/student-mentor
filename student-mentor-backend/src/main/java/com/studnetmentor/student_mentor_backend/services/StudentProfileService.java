package com.studnetmentor.student_mentor_backend.services;

import com.studnetmentor.student_mentor_backend.dto.StudentProfileRequest;
import com.studnetmentor.student_mentor_backend.dto.StudentProfileResponse;
import com.studnetmentor.student_mentor_backend.models.StudentProfile;
import com.studnetmentor.student_mentor_backend.models.User;
import com.studnetmentor.student_mentor_backend.repositories.StudentProfileRepository;
import com.studnetmentor.student_mentor_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final UserRepository userRepository;

    public StudentProfileResponse createProfile(Long userId, StudentProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        StudentProfile profile = studentProfileRepository.findByUserId(userId)
                .orElse(new StudentProfile());

        profile.setUser(user);
        profile.setGraduationYear(request.getGraduationYear());
        profile.setUniversity(request.getUniversity());
        profile.setBranch(request.getBranch());
        profile.setCgpa(request.getCgpa());
        profile.setAbout(request.getAbout());
        profile.setSkills(request.getSkills());

        profile.setLinkedinUrl(request.getLinkedinUrl());
        profile.setResumeUrl(request.getResumeUrl());

        studentProfileRepository.save(profile);

        return toResponse(profile);
    }

    public StudentProfileResponse getProfile(Long userId) {
        StudentProfile profile = studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student profile not found"));
        
        return toResponse(profile); 
    }

    private StudentProfileResponse toResponse(StudentProfile profile) {
        return StudentProfileResponse.builder()
                .profileId(profile.getId())
                .userId(profile.getUser().getId())
                .name(profile.getUser().getName())
                .university(profile.getUniversity())
                .branch(profile.getBranch())
                .cgpa(profile.getCgpa())
                .graduationYear(profile.getGraduationYear())
                .about(profile.getAbout())
                .skills(profile.getSkills())
                .linkedinUrl(profile.getLinkedinUrl())
                .resumeUrl(profile.getResumeUrl())
                .build();
    }
}