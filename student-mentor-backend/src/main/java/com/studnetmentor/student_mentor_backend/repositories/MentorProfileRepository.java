package com.studnetmentor.student_mentor_backend.repositories;

import com.studnetmentor.student_mentor_backend.models.MentorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentorProfileRepository extends JpaRepository<MentorProfile, Long> {
    
    Optional<MentorProfile> findByUserId(Long userId);

    
    @Query("SELECT m FROM MentorProfile m WHERE " +
           "(:skill IS NULL OR LOWER(m.skills) LIKE LOWER(CONCAT('%', :skill, '%'))) AND " +
           "(:company IS NULL OR LOWER(m.company) LIKE LOWER(CONCAT('%', :company, '%'))) AND " +
           "(:minExp IS NULL OR m.yearsOfExperience >= :minExp) AND " +
           "(:jobTitle IS NULL OR LOWER(m.jobTitle) LIKE LOWER(CONCAT('%', :jobTitle, '%'))) AND " +
           "(:languages IS NULL OR LOWER(m.languages) LIKE LOWER(CONCAT('%', :languages, '%')))")
    List<MentorProfile> searchMentors(
            @Param("skill") String skill,
            @Param("company") String company,
            @Param("minExp") Integer minExp,
            @Param("jobTitle") String jobTitle,
            @Param("languages") String languages
    );
}