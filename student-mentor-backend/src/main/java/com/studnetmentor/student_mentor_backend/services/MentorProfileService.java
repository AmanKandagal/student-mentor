package com.studnetmentor.student_mentor_backend.services;

import com.studnetmentor.student_mentor_backend.dto.MentorProfileRequest;
import com.studnetmentor.student_mentor_backend.dto.MentorProfileResponse;
import com.studnetmentor.student_mentor_backend.models.MentorProfile;
import com.studnetmentor.student_mentor_backend.models.User;
import com.studnetmentor.student_mentor_backend.repositories.MentorProfileRepository;
import com.studnetmentor.student_mentor_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentorProfileService {

    private final MentorProfileRepository mentorProfileRepository;
    private final UserRepository userRepository;

    public MentorProfileResponse createProfile(Long userId, MentorProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        MentorProfile profile = mentorProfileRepository.findByUserId(userId)
                .orElse(new MentorProfile());

        profile.setUser(user);
        
        profile.setCompany(request.getCompany());
        profile.setJobTitle(request.getJobTitle());
        profile.setBio(request.getBio());
        profile.setYearsOfExperience(request.getYearsOfExperience());
        profile.setSkills(request.getSkills());

        profile.setLanguages(request.getLanguages());
        profile.setLinkedinUrl(request.getLinkedinUrl());

        mentorProfileRepository.save(profile);

        return toResponse(profile);
    }

    public MentorProfileResponse getProfile(Long userId) {
        MentorProfile profile = mentorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor profile not found"));

        return toResponse(profile);
    }

    public List<MentorProfileResponse> searchMentors(String skill, String company, Integer minExp, String jobTitle, String languages) {
        return mentorProfileRepository.searchMentors(skill, company, minExp, jobTitle, languages)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private MentorProfileResponse toResponse(MentorProfile profile) {
        MentorProfileResponse response = new MentorProfileResponse();
        
        response.setUserId(profile.getUser().getId());
        response.setName(profile.getUser().getName());
        response.setEmail(profile.getUser().getEmail());
        
        response.setCompany(profile.getCompany());
        response.setJobTitle(profile.getJobTitle());
        response.setBio(profile.getBio());
        response.setYearsOfExperience(profile.getYearsOfExperience());
        response.setSkills(profile.getSkills());
        
        response.setLanguages(profile.getLanguages());
        response.setLinkedinUrl(profile.getLinkedinUrl());
        
        return response;
    }
}