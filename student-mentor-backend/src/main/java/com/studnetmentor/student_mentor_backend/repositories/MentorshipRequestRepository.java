package com.studnetmentor.student_mentor_backend.repositories;

import com.studnetmentor.student_mentor_backend.models.MentorshipRequest;
import com.studnetmentor.student_mentor_backend.models.RequestStatus;
import com.studnetmentor.student_mentor_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MentorshipRequestRepository extends JpaRepository<MentorshipRequest, Long> {

    List<MentorshipRequest> findByMentor(User mentor);
    List<MentorshipRequest> findByStudent(User student);

    List<MentorshipRequest> findByMentorAndStatus(User mentor, RequestStatus status);
    List<MentorshipRequest> findByStudentAndStatus(User student, RequestStatus status);

    boolean existsByStudentAndMentorAndStatus(User student, User mentor, RequestStatus status);
}