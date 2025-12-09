
export interface MentorshipRequestDto {
  studentId: number;
  mentorId: number;
  message: string;
}


export interface MentorshipResponse {
  requestId: number;
  message: string;
  status: 'PENDING' | 'ACCEPTED' | 'DECLINED';
  createdAt: string; 
  
  studentId: number;
  studentName: string;
  studentEmail?: string; 
  
  mentorId: number;
  mentorName: string;

  studentLinkedinUrl: string;
  studentResumeUrl: string;
}
