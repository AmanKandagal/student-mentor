export interface StudentProfileRequest {
  graduationYear: number;
  university: string;
  branch: string;
  cgpa: number;
  about: string;
  skills: string; 
  linkedinUrl: string;
  resumeUrl: string;
}

export interface StudentProfileResponse {
  profileId: number;
  userId: number;
  name: string;
  university: string;
  branch: string;
  graduationYear: number;
  cgpa: number;
  about: string;
  skills: string;
  linkedinUrl: string;
  resumeUrl: string;
}