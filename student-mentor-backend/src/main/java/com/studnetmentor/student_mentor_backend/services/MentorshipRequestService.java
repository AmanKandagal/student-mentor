package com.studnetmentor.student_mentor_backend.services;

import com.studnetmentor.student_mentor_backend.dto.MentorshipRequestDto;
import com.studnetmentor.student_mentor_backend.dto.MentorshipResponseDto;
import com.studnetmentor.student_mentor_backend.models.*;
import com.studnetmentor.student_mentor_backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentorshipRequestService {

    private final UserRepository userRepository;
    private final MentorshipRequestRepository requestRepository;
    private final StudentProfileRepository studentProfileRepository;

    private MentorshipResponseDto mapToDto(MentorshipRequest request) {
        
        String resumeUrl = null;
        String linkedinUrl = null;
        
        Optional<StudentProfile> profile = studentProfileRepository.findByUserId(request.getStudent().getId());
        
        if (profile.isPresent()) {
            resumeUrl = profile.get().getResumeUrl();
            linkedinUrl = profile.get().getLinkedinUrl();
        }

        return MentorshipResponseDto.builder()
                .requestId(request.getId())
                .message(request.getMessage())
                .status(request.getStatus())
                .createdAt(request.getCreatedAt())
                
                // Student Data (From User Entity)
                .studentId(request.getStudent().getId())
                .studentName(request.getStudent().getName())
                .studentEmail(request.getStudent().getEmail())
                
                // URLs (From StudentProfile Entity)
                .studentResumeUrl(resumeUrl)
                .studentLinkedinUrl(linkedinUrl)
                
                // Mentor Data
                .mentorId(request.getMentor().getId())
                .mentorName(request.getMentor().getName())
                .build();
    }  

    public MentorshipResponseDto sendRequest(MentorshipRequestDto dto) {
        if(dto.getStudentId().equals(dto.getMentorId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot request yourself");
        }

        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        
        User mentor = userRepository.findById(dto.getMentorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor not found"));

        boolean alreadyPending = requestRepository.existsByStudentAndMentorAndStatus(student, mentor, RequestStatus.PENDING);
        if (alreadyPending) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request already pending");
        }
        
        boolean alreadyAccepted = requestRepository.existsByStudentAndMentorAndStatus(student, mentor, RequestStatus.ACCEPTED);
        if (alreadyAccepted) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are already connected");
        }

        MentorshipRequest request = MentorshipRequest.builder()
                .student(student)
                .mentor(mentor)
                .message(dto.getMessage())
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        MentorshipRequest savedRequest = requestRepository.save(request);
        return mapToDto(savedRequest);
    }

    public List<MentorshipResponseDto> getRequestsForMentor(Long mentorId) {
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor not found"));
        return requestRepository.findByMentor(mentor).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MentorshipResponseDto> getRequestsForStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        return requestRepository.findByStudent(student).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public MentorshipResponseDto acceptRequest(Long requestId) {
        MentorshipRequest req = requestRepository.findById(requestId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        req.setStatus(RequestStatus.ACCEPTED);
        return mapToDto(requestRepository.save(req));
    }

    public MentorshipResponseDto declineRequest(Long requestId) {
        MentorshipRequest req = requestRepository.findById(requestId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        req.setStatus(RequestStatus.DECLINED);
        return mapToDto(requestRepository.save(req));
    }
    
    public List<MentorshipResponseDto> getPendingForMentor(Long mentorId) {
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor not found"));
        return requestRepository.findByMentorAndStatus(mentor, RequestStatus.PENDING).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MentorshipResponseDto> getAcceptedForMentor(Long mentorId) {
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor not found"));
        return requestRepository.findByMentorAndStatus(mentor, RequestStatus.ACCEPTED).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MentorshipResponseDto> getDeclinedForMentor(Long mentorId) {
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentor not found"));
        return requestRepository.findByMentorAndStatus(mentor, RequestStatus.DECLINED).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}