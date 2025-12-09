export type Role = 'STUDENT' | 'MENTOR';

export interface LoginRequest {
  email?: string;
  password?: string;
}

export interface RegisterRequest {
  name?: string;
  email?: string;
  password?: string;
  role?: Role;
}

export interface AuthResponse {
  token: string;
}