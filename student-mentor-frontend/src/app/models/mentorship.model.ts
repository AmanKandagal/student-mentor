// 1. Request DTO (Sending to Backend)
// We removed the URLs here because the Backend now fetches them automatically from the Student Profile
export interface MentorshipRequestDto {
  studentId: number;
  mentorId: number;
  message: string;
}

// 2. Response DTO (Receiving from Backend)
// We KEEP the URLs here because the Mentor needs to see them in the Dashboard
export interface MentorshipResponse {
  requestId: number;
  message: string;
  status: 'PENDING' | 'ACCEPTED' | 'DECLINED';
  createdAt: string; 
  
  // Flattened data from Backend DTO
  studentId: number;
  studentName: string;
  studentEmail?: string; // Optional: good to have if backend sends it
  
  mentorId: number;
  mentorName: string;

  // New Fields for Review (Fetched from Profile)
  studentLinkedinUrl: string;
  studentResumeUrl: string;
}