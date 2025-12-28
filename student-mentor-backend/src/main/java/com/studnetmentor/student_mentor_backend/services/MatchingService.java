package com.studnetmentor.student_mentor_backend.services;

import com.studnetmentor.student_mentor_backend.models.*;
import com.studnetmentor.student_mentor_backend.repositories.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MentorProfileRepository mentorRepository;
    private final MentorshipRequestRepository requestRepository;

    private final Map<String, List<Long>> skillIndex = new ConcurrentHashMap<>();

    private final Map<Long, Set<Long>> connectionGraph = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadDataStructures() {
        System.out.println("🚀 OPTIMIZATION: Loading Data Structures into Memory...");
        refreshSkillIndex();
        refreshConnectionGraph();
        System.out.println("✅ OPTIMIZATION: Data Structures Loaded.");
    }

    public void refreshSkillIndex() {
        skillIndex.clear();
        List<MentorProfile> allMentors = mentorRepository.findAll();
        for (MentorProfile mentor : allMentors) {
            if (mentor.getSkills() != null && !mentor.getSkills().isEmpty()) {
                String[] skills = mentor.getSkills().split(",");
                for (String skill : skills) {
                    skillIndex.computeIfAbsent(skill.trim().toLowerCase(), k -> new ArrayList<>()).add(mentor.getId());
                }
            }
        }
    }

    public void refreshConnectionGraph() {
        connectionGraph.clear();
        List<MentorshipRequest> allRequests = requestRepository.findAll();
        for (MentorshipRequest req : allRequests) {
            Long studentId = req.getStudent().getId();
            Long mentorId = req.getMentor().getId();
            connectionGraph.computeIfAbsent(studentId, k -> new HashSet<>()).add(mentorId);
        }
    }

    public List<Long> findMentorIdsBySkill(String skill) {
        return skillIndex.getOrDefault(skill.toLowerCase(), Collections.emptyList());
    }

    public boolean isAlreadyConnected(Long studentId, Long mentorId) {
        if (connectionGraph.containsKey(studentId)) {
            return connectionGraph.get(studentId).contains(mentorId);
        }
        return false;
    }

    public void updateIndex(MentorProfile profile) {
        if (profile.getSkills() != null) {
            String[] skills = profile.getSkills().split(",");
            for (String skill : skills) {
                skillIndex.computeIfAbsent(skill.trim().toLowerCase(), k -> new ArrayList<>()).add(profile.getId());
            }
        }
    }

    public void addConnection(Long studentId, Long mentorId) {
        connectionGraph.computeIfAbsent(studentId, k -> new HashSet<>()).add(mentorId);
        System.out.println("🔗 Graph Updated: Student " + studentId + " connected to Mentor " + mentorId);
    }
}