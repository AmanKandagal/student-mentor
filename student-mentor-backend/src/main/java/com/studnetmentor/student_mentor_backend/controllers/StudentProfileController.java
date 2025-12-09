package com.studnetmentor.student_mentor_backend.controllers;

import com.studnetmentor.student_mentor_backend.dto.StudentProfileRequest;
import com.studnetmentor.student_mentor_backend.dto.StudentProfileResponse;
import com.studnetmentor.student_mentor_backend.services.StudentProfileService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @PostMapping("/{userId}/profile")
    public StudentProfileResponse createProfile(
            @PathVariable Long userId,
            @RequestBody StudentProfileRequest request
    ) {
        return studentProfileService.createProfile(userId, request);
    }

    @GetMapping("/{userId}/profile")
    public StudentProfileResponse getProfile(@PathVariable Long userId) {
        return studentProfileService.getProfile(userId);
    }
}
