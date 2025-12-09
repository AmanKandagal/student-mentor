package com.studnetmentor.student_mentor_backend.controllers;

import com.studnetmentor.student_mentor_backend.dto.MentorProfileRequest;
import com.studnetmentor.student_mentor_backend.dto.MentorProfileResponse;
import com.studnetmentor.student_mentor_backend.services.MentorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mentors")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MentorProfileController {

    private final MentorProfileService mentorProfileService;

    @PostMapping("/{userId}/profile")
    public MentorProfileResponse createMentorProfile(
            @PathVariable Long userId,
            @RequestBody MentorProfileRequest request
    ) {
        return mentorProfileService.createProfile(userId, request);
    }
    
    @GetMapping("/{userId}/profile")
    public MentorProfileResponse getProfile(@PathVariable Long userId) {
        return mentorProfileService.getProfile(userId);
    }

}
