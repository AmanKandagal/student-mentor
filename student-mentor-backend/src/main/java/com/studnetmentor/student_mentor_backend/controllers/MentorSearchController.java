package com.studnetmentor.student_mentor_backend.controllers;

import com.studnetmentor.student_mentor_backend.dto.MentorProfileResponse;
import com.studnetmentor.student_mentor_backend.services.MentorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mentors/search")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MentorSearchController {

    private final MentorProfileService mentorProfileService;

    
    @GetMapping
    public List<MentorProfileResponse> searchMentors(
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) Integer minExp,
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) String languages
    ) {
        return mentorProfileService.searchMentors(skill, company, minExp, jobTitle, languages);
    }
}