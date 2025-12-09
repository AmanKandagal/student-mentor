export interface MentorProfileRequest {
  company: string;
  jobTitle: string;
  bio: string;
  yearsOfExperience: number;
  skills: string;
  languages: string;
  linkedinUrl: string;
}

export interface MentorProfileResponse {
  profileId: number;
  userId: number;
  name: string;
  email: string;
  company: string;
  jobTitle: string;
  bio: string;
  yearsOfExperience: number;
  skills: string;
  languages: string;
  linkedinUrl: string;
}

export interface MentorFilter {
  searchQuery: string;
  minExperience: number;
  company: string;
  language: string;
  jobTitle: string;
}