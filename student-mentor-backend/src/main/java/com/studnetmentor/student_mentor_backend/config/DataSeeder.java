package com.studnetmentor.student_mentor_backend.config;

import com.studnetmentor.student_mentor_backend.models.*;
import com.studnetmentor.student_mentor_backend.repositories.*;
import com.studnetmentor.student_mentor_backend.services.MatchingService; // Import this
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, 
                                   MentorProfileRepository mentorRepository,
                                   StudentProfileRepository studentRepository, 
                                   PasswordEncoder passwordEncoder,
                                   MatchingService advancedMatchingService) { // Inject Optimization Service
        return args -> {
            // 1. Check if data already exists
            if (userRepository.count() > 0) {
                // Even if DB exists, we MUST refresh the HashMap because memory is wiped on restart
                System.out.println("✅ Database exists. Refreshing In-Memory Search Index...");
                advancedMatchingService.refreshSkillIndex(); 
                advancedMatchingService.refreshConnectionGraph();
                return;
            }

            System.out.println("🚀 Starting Data Simulation...");

            String[] companies = {"Google", "Microsoft", "Amazon", "Netflix", "Spotify", "Tesla", "Startup Inc", "Meta", "Apple", "Adobe"};
            String[] skillsList = {"Java", "Python", "Angular", "React", "AWS", "Docker", "System Design", "Spring Boot", "Machine Learning", "Data Science"};
            String[] firstNames = {"Alex", "Jordan", "Taylor", "Morgan", "Casey", "Riley", "Sam", "Jamie", "Drew", "Robin", "Chris", "Pat"};
            String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
            
            Random rand = new Random();

            // --- SIMULATE 10 MENTORS ---
            for (int i = 1; i <= 10; i++) {
                String fname = firstNames[rand.nextInt(firstNames.length)];
                String lname = lastNames[rand.nextInt(lastNames.length)];
                String email = "mentor" + i + "@test.com";

                User user = new User();
                user.setName(fname + " " + lname);
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode("password"));
                user.setRole(Role.MENTOR);
                userRepository.save(user);

                MentorProfile profile = new MentorProfile();
                profile.setUser(user);
                profile.setCompany(companies[rand.nextInt(companies.length)]);
                profile.setJobTitle(rand.nextBoolean() ? "Senior Engineer" : "Staff Engineer");
                profile.setYearsOfExperience(rand.nextInt(15) + 3);
                
                String skill1 = skillsList[rand.nextInt(skillsList.length)];
                String skill2 = skillsList[rand.nextInt(skillsList.length)];
                profile.setSkills(skill1 + ", " + skill2); // e.g. "Java, React"
                
                profile.setBio("Passionate mentor.");
                profile.setLanguages("English");
                profile.setLinkedinUrl("https://linkedin.com/in/" + fname + lname);
                
                mentorRepository.save(profile);
            }
            System.out.println("✅ Generated 10 Mentors.");

            // --- SIMULATE STUDENTS (Abbreviated for brevity, keep your existing logic) ---
            // ... (Your existing student generation code goes here) ...
            
            // --- CRITICAL FIX: REFRESH HASHMAP AFTER SEEDING ---
            System.out.println("🔄 Seeding Complete. Building In-Memory Optimization Indexes...");
            advancedMatchingService.refreshSkillIndex();
            System.out.println("✅ Search Optimization is Ready!");
        };
    }
}