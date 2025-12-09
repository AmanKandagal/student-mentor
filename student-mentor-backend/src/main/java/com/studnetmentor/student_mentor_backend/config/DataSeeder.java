package com.studnetmentor.student_mentor_backend.config;

import com.studnetmentor.student_mentor_backend.models.*;
import com.studnetmentor.student_mentor_backend.repositories.*;
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
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Check if data already exists (so we don't duplicate on every restart)
            if (userRepository.count() > 0) {
                System.out.println("✅ Database already contains data. Skipping Seeder.");
                return;
            }

            System.out.println("🚀 Starting Data Simulation: Generating 10 Mentors and 30 Mentees...");

            String[] companies = {"Google", "Microsoft", "Amazon", "Netflix", "Spotify", "Tesla", "Startup Inc", "Meta", "Apple", "Adobe"};
            String[] skillsList = {"Java", "Python", "Angular", "React", "AWS", "Docker", "System Design", "Spring Boot", "Machine Learning", "Data Science"};
            String[] firstNames = {"Alex", "Jordan", "Taylor", "Morgan", "Casey", "Riley", "Sam", "Jamie", "Drew", "Robin", "Chris", "Pat"};
            String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
            
            Random rand = new Random();

            // --- SIMULATE 10 MENTORS ---
            for (int i = 1; i <= 10; i++) {
                String fname = firstNames[rand.nextInt(firstNames.length)];
                String lname = lastNames[rand.nextInt(lastNames.length)];
                String email = "mentor" + i + "@test.com"; // predictable emails: mentor1@test.com

                // 1. Create User
                User user = new User();
                user.setName(fname + " " + lname);
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode("password")); // Easy password for testing
                user.setRole(Role.MENTOR);
                userRepository.save(user);

                // 2. Create Profile
                MentorProfile profile = new MentorProfile();
                profile.setUser(user);
                profile.setCompany(companies[rand.nextInt(companies.length)]);
                profile.setJobTitle(rand.nextBoolean() ? "Senior Engineer" : "Staff Engineer");
                profile.setYearsOfExperience(rand.nextInt(15) + 3); // 3 to 18 years
                
                // Generate varied skills
                String skill1 = skillsList[rand.nextInt(skillsList.length)];
                String skill2 = skillsList[rand.nextInt(skillsList.length)];
                profile.setSkills(skill1 + ", " + skill2);
                
                profile.setBio("Passionate mentor with experience in " + skill1 + ". I help students crack interviews.");
                profile.setLanguages(rand.nextBoolean() ? "English, Spanish" : "English, Hindi");
                profile.setLinkedinUrl("https://linkedin.com/in/" + fname.toLowerCase() + lname.toLowerCase());
                
                mentorRepository.save(profile);
            }
            System.out.println("✅ Generated 10 Mentors.");

            // --- SIMULATE 30 MENTEES (STUDENTS) ---
            for (int i = 1; i <= 30; i++) {
                String fname = firstNames[rand.nextInt(firstNames.length)];
                String lname = lastNames[rand.nextInt(lastNames.length)];
                String email = "student" + i + "@test.com"; // predictable emails: student1@test.com

                // 1. Create User
                User student = new User();
                student.setName(fname + " " + lname);
                student.setEmail(email);
                student.setPassword(passwordEncoder.encode("password"));
                student.setRole(Role.STUDENT);
                userRepository.save(student);

                // 2. Create Profile
                StudentProfile profile = new StudentProfile();
                profile.setUser(student);
                profile.setUniversity("Tech University " + (rand.nextInt(5) + 1));
                profile.setBranch("Computer Science");
                profile.setGraduationYear(2024 + rand.nextInt(3));
                profile.setCgpa(7.0 + (rand.nextDouble() * 3.0)); // Random CGPA 7.0 - 10.0
                profile.setSkills("Java, C++");
                profile.setAbout("Eager to learn and find a mentor.");
                
                // --- ADDED: Fake URLs for testing ---
                profile.setLinkedinUrl("https://linkedin.com/in/" + fname.toLowerCase() + lname.toLowerCase());
                profile.setResumeUrl("https://docs.google.com/resume/" + fname.toLowerCase() + lname.toLowerCase());
                
                studentRepository.save(profile);
            }
            System.out.println("✅ Generated 30 Students.");
            
            System.out.println("🎉 LOAD SIMULATION COMPLETE!");
        };
    }
}