package com.studnetmentor.student_mentor_backend.controllers;

import com.studnetmentor.student_mentor_backend.dto.MentorshipRequestDto;
import com.studnetmentor.student_mentor_backend.dto.MentorshipResponseDto;
import com.studnetmentor.student_mentor_backend.services.MentorshipRequestService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MentorshipRequestController {

    private final MentorshipRequestService requestService;

    @PostMapping("/send")
    public MentorshipResponseDto send(@RequestBody MentorshipRequestDto dto) {
        return requestService.sendRequest(dto);
    }
    
    @GetMapping("/mentor/{mentorId}")
    public List<MentorshipResponseDto> getMentorRequests(@PathVariable Long mentorId) {
        return requestService.getRequestsForMentor(mentorId);
    }
    
    @PatchMapping("/{requestId}/accept")
    public MentorshipResponseDto acceptRequest(@PathVariable Long requestId) {
        return requestService.acceptRequest(requestId);
    }
    
    @PatchMapping("/{requestId}/decline")
    public MentorshipResponseDto declineRequest(@PathVariable Long requestId) {
        return requestService.declineRequest(requestId);
    }
    
    @GetMapping("/student/{studentId}")
    public List<MentorshipResponseDto> studentRequests(@PathVariable Long studentId) {
        return requestService.getRequestsForStudent(studentId);
    }
    
    @GetMapping("/mentor/{mentorId}/pending")
    public List<MentorshipResponseDto> getPendingForMentor(@PathVariable Long mentorId) {
        return requestService.getPendingForMentor(mentorId);
    }

    @GetMapping("/mentor/{mentorId}/accepted")
    public List<MentorshipResponseDto> getAcceptedForMentor(@PathVariable Long mentorId) {
        return requestService.getAcceptedForMentor(mentorId);
    }

    @GetMapping("/mentor/{mentorId}/declined")
    public List<MentorshipResponseDto> getDeclinedForMentor(@PathVariable Long mentorId) {
        return requestService.getDeclinedForMentor(mentorId);
    }
}